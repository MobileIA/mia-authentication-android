package com.mobileia.authentication;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.mobileia.authentication.auth.TwitterAuth;
import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.realm.AuthenticationRealm;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;

import java.util.Collection;

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
     * Instancia de Google Signin
     */
    protected AuthGoogle mAuthGoogle;

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
     * Iniciar sesión con Facebook
     * @param activity
     * @param callback
     */
    public void signInWithFacebook(Activity activity, LoginResult callback){
        new AuthFacebook(activity).signIn(callback);
    }

    /**
     * Iniciar sesión con Facebook seteando los permisos
     * @param activity
     * @param permissions
     * @param callback
     */
    public void signInWithFacebook(Activity activity, Collection<String> permissions, LoginResult callback){
        AuthFacebook auth = new AuthFacebook(activity);
        auth.setPermissions(permissions);
        auth.signIn(callback);
    }

    /**
     * Iniciar sesión con Google
     * @param activity
     * @param callback
     */
    public void signInWithGoogle(FragmentActivity activity, String googleId, LoginResult callback){
        mAuthGoogle = new AuthGoogle(activity);
        mAuthGoogle.signIn(googleId, callback);
    }

    /**
     * Iniciar sesión con Twitter
     * @param activity
     * @param callback
     */
    public void signInWithTwitter(Activity activity, LoginResult callback){
        new TwitterAuth(activity).signIn(callback);
    }

    /**
     * Funcion para realizar un login con email y password
     * @param email
     * @param password
     * @param callback
     */
    public void signInWithEmailAndPassword(String email, String password, LoginResult callback){
        new RestGenerator().signIn(email, password, callback);
    }

    /**
     * Funcionalidad que se encarga de registrar una nueva cuenta
     * @param user
     * @param password
     * @param callback
     */
    public void createAccount(final User user, final String password, final RegisterResult callback){
        // Registramos la nueva cuenta
        new RestGenerator().register(user, password, new RegisterResult() {
            @Override
            public void onSuccess(int userId) {
                // Guardamos ID del usuario
                user.setId(userId);
                // Pedimos un AccessToken
                new RestGenerator().oauth(user.getEmail(), password, new AccessTokenResult() {
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
                    public void onError(Error error) {
                        // Se produjo un error
                        callback.onError(error);
                    }
                });
            }

            @Override
            public void onError(Error error) {
                // Se produjo un error
                callback.onError(error);
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
     * Obtiene instancia de AuthGoogle
     * @return
     */
    public AuthGoogle getAuthGoogle(){ return mAuthGoogle; }

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
