package com.mobileia.authentication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.v4.app.FragmentActivity;

import com.mobileia.authentication.core.MobileiaAuthBase;
import com.mobileia.authentication.core.entity.User;
import com.mobileia.authentication.core.listener.AccessTokenResult;
import com.mobileia.authentication.core.listener.LoginResult;
import com.mobileia.authentication.core.realm.AuthBaseRealm;
import com.mobileia.authentication.listener.RecoveryResult;
import com.mobileia.authentication.core.listener.RegisterResult;
import com.mobileia.authentication.listener.UpdateResult;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;

import java.util.Collection;

import io.realm.Realm;

/**
 * Created by matiascamiletti on 31/7/17.
 */
@Keep
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
    protected MobileiaAuthBase mAuthService;

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
     * Iniciar sesion con el servicio pasado
     * @param service
     * @param callback
     */
    public void signInWith(MobileiaAuthBase service, LoginResult callback){
        // Iniciamos sesion a traves del servicio enviado
        service.signIn(callback);
    }

    /**
     * Iniciar sesion con Facebook
     * @param activity
     * @param callback
     */
    public void signInWithFacebook(Activity activity, LoginResult callback){
        new AuthFacebook(activity).signIn(callback);
    }

    /**
     * Iniciar sesion con Facebook seteando los permisos
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
                        AuthBaseRealm.getInstance().save(user);
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
     * Funcion para recuperar una cuenta
     * @param email
     * @param password
     * @param callback
     */
    public void recoveryWithEmailAndPassword(String email, String password, RecoveryResult callback){
        new RestGenerator().recovery(email, password, callback);
    }

    /**
     * Funcion que registra la ubicacion del dispositivo
     * @param latitude
     * @param longitude
     */
    public void registerLocationThisDevice(double latitude, double longitude){
        // Verificamos si esta logueado
        if(MobileiaAuth.getInstance(mContext).getCurrentUser() == null){
            return;
        }
        // Llamar al servidor
        new RestGenerator().locationRegister(MobileiaAuth.getInstance(mContext).getCurrentUser().getAccessToken(), latitude, longitude);
    }

    /**
     * Funcion que se encarga de actualizar los datos del usuario
     * @param user
     * @param callback
     */
    public void updateUser(final User user, final UpdateResult callback){
        // Llamar al servidor
        new RestGenerator().update(MobileiaAuth.getInstance(mContext).getCurrentUser().getAccessToken(), user, new UpdateResult() {
            @Override
            public void onSuccess(int userId) {
                // Guardamos usuario en la DB
                AuthBaseRealm.getInstance().update(user);
                // Llamamos al callback
                callback.onSuccess(userId);
            }

            @Override
            public void onError(Error error) {
                // Llamamos al callback
                callback.onError(error);
            }
        });
    }

    /**
     * Funcionalidad para actualizar el DeviceToken del dispositivo
     */
    public void updateDeviceToken(){
        // Ver si se encuentra logueado
        User user = MobileiaAuth.getInstance(mContext).getCurrentUser();
        if(user == null){
            return;
        }
        new RestGenerator().updateDeviceToken(user.getAccessToken());
    }

    /**
     * Funcionalidad para cerrar sesion.
     */
    public void logoutUser(){
        // Obtener usuario logueado
        User user = getCurrentUser();
        // Verificar si existe
        if(user == null){
            return;
        }
        // Eliminar AccessToken
        new RestGenerator().logout(user.getAccessToken());
        // Eliminar usuario guardado
        AuthBaseRealm.getInstance().deleteUser(user);
    }

    /**
     * Devuelve el usuario logueado si existe
     * @return
     */
    public User getCurrentUser(){
        return AuthBaseRealm.getInstance().fetchUser();
    }

    /**
     * Obtiene instancia de AuthGoogle
     * @return
     */
    public MobileiaAuthBase getAuthService(){ return mAuthService; }

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
