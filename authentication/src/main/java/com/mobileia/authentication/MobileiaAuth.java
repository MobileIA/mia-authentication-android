package com.mobileia.authentication;

import com.mobileia.authentication.core.MobileiaAuthBase;
import com.mobileia.authentication.core.entity.User;
import com.mobileia.authentication.core.listener.AccessTokenResult;
import com.mobileia.authentication.core.listener.LoginResult;
import com.mobileia.authentication.core.realm.AuthBaseRealm;
import com.mobileia.authentication.core.rest.AuthRestBase;
import com.mobileia.authentication.listener.RecoveryResult;
import com.mobileia.authentication.core.listener.RegisterResult;
import com.mobileia.authentication.listener.UpdateResult;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class MobileiaAuth {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static MobileiaAuth sOurInstance = null;
    /**
     * Instancia de Google Signin
     */
    protected MobileiaAuthBase mAuthService;

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static MobileiaAuth getInstance() {
        if(sOurInstance == null){
            sOurInstance = new MobileiaAuth();
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
     * Funcion para sincronizar el perfil del usuario
     */
    public void syncProfile(){
        new AuthRestBase().me(getCurrentUser().getAccessToken(), new LoginResult() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onError(Error error) {

            }
        });
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
        if(getCurrentUser() == null){
            return;
        }
        // Llamar al servidor
        new RestGenerator().locationRegister(getCurrentUser().getAccessToken(), latitude, longitude);
    }

    /**
     * Funcion que se encarga de actualizar los datos del usuario
     * @param user
     * @param callback
     */
    public void updateUser(final User user, final UpdateResult callback){
        // Llamar al servidor
        new RestGenerator().update(getCurrentUser().getAccessToken(), user, new UpdateResult() {
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
        User user = getCurrentUser();
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
     */
    private MobileiaAuth() {

    }
}
