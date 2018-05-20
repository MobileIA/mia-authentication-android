package com.mobileia.authentication.rest;

import com.mobileia.authentication.core.rest.AuthRestBase;
import com.mobileia.authentication.core.entity.AccessToken;
import com.mobileia.authentication.core.entity.User;
import com.mobileia.authentication.core.listener.AccessTokenResult;
import com.mobileia.authentication.core.listener.LoginResult;
import com.mobileia.authentication.listener.RecoveryResult;
import com.mobileia.authentication.core.listener.RegisterResult;
import com.mobileia.authentication.listener.UpdateResult;
import com.mobileia.core.Mobileia;
import com.mobileia.core.entity.Error;
import com.mobileia.core.rest.RestBody;
import com.mobileia.core.rest.RestBodyCall;
import com.mobileia.core.rest.RestBodyCallback;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class RestGenerator extends AuthRestBase {

    /**
     * Funcion que se encarga de realizar un login a traves del email y password
     * @param email
     * @param password
     * @param callback
     */
    public void signIn(String email, String password, final LoginResult callback){
        // Geranamos request
        oauth(email, password, new AccessTokenResult() {
            @Override
            public void onSuccess(String accessToken) {
                // Pedimos la informacion del usuario
                me(accessToken, callback);
            }

            @Override
            public void onError(Error error) {
                // Llamamos al callback porque hubo error
                callback.onError(error);
            }
        });
    }
    /**
     * Funcion que se encarga de pedir el accessToken del usuario
     * @param email
     * @param password
     * @param callback
     */
    public void oauth(String email, String password, final AccessTokenResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos request
        RestBodyCall<AccessToken> call = service.oauth(Mobileia.getInstance().getAppId(), "normal", email, password, Mobileia.getInstance().getDeviceToken(), Mobileia.getInstance().getDeviceName(), 0, Locale.getDefault().getLanguage(), "1.0");
        // Ejecutamos request
        oauthExecuteCall(call, callback);
    }

    /**
     * Hace un request para registrar un usuario con los datos ingresados
     * @param user
     * @param password
     * @param callback
     */
    public void register(User user, String password, final RegisterResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos Request
        RestBodyCall<User> call = service.register(Mobileia.getInstance().getAppId(), "normal", user.getEmail(), password, user.getFirstname(), user.getLastname(), user.getPhoto(), user.getPhone());
        // Ejecutamos la request
        registerExecuteCall(call, callback);
    }

    /**
     * Funcionalidad para enviar un email asi puede recuperar la password
     * @param email
     * @param password
     * @param callback
     */
    public void recovery(String email, String password, final RecoveryResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // generamos Request
        RestBodyCall<Boolean> call = service.recovery(Mobileia.getInstance().getAppId(), email, password);
        // Ejecutamos la call
        call.enqueue(new RestBodyCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean body) {
                // Llamamos al callback con exito
                callback.onSuccess();
            }

            @Override
            public void onError(Error error) {
                // Llamamos al callback porque hubo error
                callback.onError(error);
            }
        });
    }

    /**
     * Servicio que registra la localizacion del dispositivo
     * @param accessToken
     * @param latitude
     * @param longitude
     */
    public void locationRegister(String accessToken, double latitude, double longitude){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // generamos Request
        RestBodyCall<Boolean> call = service.locationRegister(Mobileia.getInstance().getAppId(), accessToken, latitude, longitude);
        // Ejecutamos la call
        call.enqueue(new RestBodyCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean body) {

            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    /**
     * Funcion para actualizar los datos del usuario
     * @param accessToken
     * @param user
     */
    public void update(String accessToken, User user, final UpdateResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // generamos Request
        RestBodyCall<User> call = service.update(Mobileia.getInstance().getAppId(), accessToken, user.getFirstname(), user.getLastname(), user.getPhoto(), user.getPhone());
        // Ejecutamos la call
        call.enqueue(new RestBodyCallback<User>() {
            @Override
            public void onSuccess(User body) {
                callback.onSuccess(body.getId());
            }

            @Override
            public void onError(Error error) {
                // Llamamos al callback porque hubo error
                callback.onError(error);
            }
        });
    }

    /**
     * Llama al servidor para cerra sesion y eliminar AccessToken
     * @param accessToken
     */
    public void logout(String accessToken){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // generamos Request
        RestBodyCall<Boolean> call = service.logout(Mobileia.getInstance().getAppId(), accessToken);
        // Ejecutamos la call
        call.enqueue(new RestBodyCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean body) {

            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    /**
     * Servicio que actualiza el token del dispositivo.
     * @param accessToken
     */
    public void updateDeviceToken(String accessToken){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // generamos Request
        RestBodyCall<Boolean> call = service.updateDeviceToken(Mobileia.getInstance().getAppId(), accessToken, Mobileia.getInstance().getDeviceToken(), Mobileia.getInstance().getDeviceName(), 0, Locale.getDefault().getLanguage(), "1.0");
        // Ejecutamos la call
        call.enqueue(new RestBodyCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean body) {

            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    /**
     * Funcion que se encarga de ejecutar la request Oauth de todos los tipos
     * @param call
     * @param callback
     */
    protected void oauthExecuteCall(RestBodyCall<AccessToken> call, final AccessTokenResult callback){
        call.enqueue(new RestBodyCallback<AccessToken>() {
            @Override
            public void onSuccess(AccessToken body) {
                // Enviamos el accessToken obtenido
                callback.onSuccess(body.access_token);
            }

            @Override
            public void onError(Error error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Funcion que se encarga de ejecutar la request para registro
     * @param call
     * @param callback
     */
    protected void registerExecuteCall(RestBodyCall<User> call, final RegisterResult callback){
        call.enqueue(new RestBodyCallback<User>() {
            @Override
            public void onSuccess(User body) {
                callback.onSuccess(body.getId());
            }

            @Override
            public void onError(Error error) {
                callback.onError(error);
            }
        });
    }
}
