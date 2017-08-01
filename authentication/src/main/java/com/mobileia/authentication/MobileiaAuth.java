package com.mobileia.authentication;

import android.content.Context;

import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.realm.AuthenticationRealm;
import com.mobileia.authentication.rest.RestGenerator;

import io.realm.Realm;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class MobileiaAuth {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static MobileiaAuth sOurInstance = null;
    /**
     * Almacena el contexto
     */
    protected Context mContext;

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static MobileiaAuth getInstance(Context context) {
        if(sOurInstance == null){
            sOurInstance = new MobileiaAuth(context.getApplicationContext());
        }
        return sOurInstance;
    }

    /**
     * Funcion para realizar un login con email y password
     * @param email
     * @param password
     * @param callback
     */
    public void signInWithEmailAndPassword(String email, String password, LoginResult callback){
        RestGenerator.signIn(email, password, callback);
    }

    /**
     * Funcionalidad que se encarga de registrar una nueva cuenta
     * @param user
     * @param password
     * @param callback
     */
    public void createAccount(final User user, final String password, final RegisterResult callback){
        // Registramos la nueva cuenta
        RestGenerator.createAccount(user, password, new RegisterResult() {
            @Override
            public void onSuccess(int userId) {
                // Guardamos ID del usuario
                user.setId(userId);
                // Pedimos el AccessToken del usuario registrado
                RestGenerator.requestAccessToken(user.getEmail(), password, new AccessTokenResult() {
                    @Override
                    public void onSuccess(String accessToken) {
                        // Guardamos el AccessToken
                        user.setAccessToken(accessToken);
                        // Guardamos usuario en la DB
                        AuthenticationRealm.getInstance().save(user);
                        // Llamamos Success
                        callback.onSuccess(user.getId());
                    }

                    @Override
                    public void onError() {
                        // Se produjo un error
                        callback.onError();
                    }
                });
            }

            @Override
            public void onError() {
                // Se produjo un error
                callback.onError();
            }
        });
    }

    /**
     * Devuelve el usuario logueado si existe
     * @return
     */
    public User getCurrentUser(){
        return AuthenticationRealm.getInstance().fetchUser();
    }

    /**
     * Constructor del singleton
     * @param context
     */
    private MobileiaAuth(Context context) {
        // Guardamos contexto
        this.mContext = context;
        // Iniciamos Realm
        Realm.init(context);
    }
}
