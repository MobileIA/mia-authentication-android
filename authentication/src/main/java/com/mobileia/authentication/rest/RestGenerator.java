package com.mobileia.authentication.rest;

import com.mobileia.authentication.entity.AccessToken;
import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RecoveryResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.listener.UpdateResult;
import com.mobileia.authentication.realm.AuthenticationRealm;
import com.mobileia.core.Mobileia;
import com.mobileia.core.entity.Error;
import com.mobileia.core.rest.RestBody;
import com.mobileia.core.rest.RestBodyCall;
import com.mobileia.core.rest.RestBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class RestGenerator extends RestBuilder {

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
        RestBodyCall<AccessToken> call = service.oauth(Mobileia.getInstance().getAppId(), "normal", email, password);
        // Ejecutamos request
        oauthExecuteCall(call, callback);
    }
    /**
     * Hace un request para generar un AccessToken desde uan cuenta de facebook
     * @param facebookId
     * @param facebookAccessToken
     * @param callback
     */
    public void oauthFacebook(String facebookId, String facebookAccessToken, final AccessTokenResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos request
        RestBodyCall<AccessToken> call = service.oauthWithFacebook(Mobileia.getInstance().getAppId(), "facebook", facebookId, facebookAccessToken);
        // Ejecutamos request
        oauthExecuteCall(call, callback);
    }
    /**
     * Hace un request para generar un AccessToken desde una cuenta de Google
     * @param googleToken
     * @param callback
     */
    public void oauthGoogle(String googleToken, final AccessTokenResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos request
        RestBodyCall<AccessToken> call = service.oauthWithGoogle(Mobileia.getInstance().getAppId(), "google", googleToken);
        // Ejecutamos request
        oauthExecuteCall(call, callback);
    }
    /**
     * Hace un request para generar un AccessToken desde una cuenta de twitter
     * @param twitterToken
     * @param twitterSecret
     * @param callback
     */
    public void oauthTwitter(String twitterToken, String twitterSecret, final AccessTokenResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos request
        RestBodyCall<AccessToken> call = service.oauthWithTwitter(Mobileia.getInstance().getAppId(), "twitter", twitterToken, twitterSecret);
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
        /*params.addProperty("device_token", Mobileia.getInstance().getDeviceToken());
        params.addProperty("device_model", Mobileia.getInstance().getDeviceName());
        params.addProperty("platform", 0);
        params.addProperty("language", Locale.getDefault().getLanguage());
        params.addProperty("version", BuildConfig.VERSION_NAME);*/
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos Request
        RestBodyCall<User> call = service.register(Mobileia.getInstance().getAppId(), "normal", user.getEmail(), password, user.getFirstname(), user.getLastname(), user.getPhoto(), user.getPhone());
        // Ejecutamos la request
        registerExecuteCall(call, callback);
    }
    /**
     * Funcionalidad para registra una cuenta con Facebook
     * @param facebookId
     * @param facebookAccessToken
     * @param callback
     */
    public void registerWithFacebook(String facebookId, String facebookAccessToken, final RegisterResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos Request
        RestBodyCall<User> call = service.registerWithFacebook(Mobileia.getInstance().getAppId(), "facebook", facebookId, facebookAccessToken);
        // Ejecutamos la request
        registerExecuteCall(call, callback);
    }
    /**
     * Funcionalidad para registra una cuenta con Google
     * @param googleToken
     * @param callback
     */
    public void registerWithGoogle(String googleToken, final RegisterResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos Request
        RestBodyCall<User> call = service.registerWithGoogle(Mobileia.getInstance().getAppId(), "google", googleToken);
        // Ejecutamos la request
        registerExecuteCall(call, callback);
    }
    /**
     * Funcionalidad para registra una cuenta con Facebook
     * @param twitterToken
     * @param twitterSecret
     * @param callback
     */
    public void registerWithTwitter(String twitterToken, String twitterSecret, final RegisterResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos Request
        RestBodyCall<User> call = service.registerWithTwitter(Mobileia.getInstance().getAppId(), "twitter", twitterToken, twitterSecret);
        // Ejecutamos la request
        registerExecuteCall(call, callback);
    }
    /**
     * Funcion que se encarga de obtener los datos del usuario a traves del AccessToken
     * @param accessToken
     * @param callback
     */
    public void me(final String accessToken, final LoginResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // generamos Request
        RestBodyCall<User> call = service.me(Mobileia.getInstance().getAppId(), accessToken);
        call.enqueue(new Callback<RestBody<User>>() {
            @Override
            public void onResponse(Call<RestBody<User>> call, Response<RestBody<User>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError(response.body().error);
                    return;
                }
                // Obtenemos usuario
                User user = response.body().response;
                // Guardamos el AccessToken
                user.setAccessToken(accessToken);
                // Guardamos usuario en la DB
                AuthenticationRealm.getInstance().save(user);
                // Llamamos al callback con exito
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Call<RestBody<User>> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError(new Error(-1, "No se pudo obtener el perfil del usuario"));
            }
        });
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
        call.enqueue(new Callback<RestBody<Boolean>>() {
            @Override
            public void onResponse(Call<RestBody<Boolean>> call, Response<RestBody<Boolean>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError(response.body().error);
                    return;
                }
                // Llamamos al callback con exito
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<RestBody<Boolean>> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError(new Error(-1, "No se pudo recuperar la cuenta"));
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
        call.enqueue(new Callback<RestBody<Boolean>>() {
            @Override
            public void onResponse(Call<RestBody<Boolean>> call, Response<RestBody<Boolean>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    return;
                }
            }

            @Override
            public void onFailure(Call<RestBody<Boolean>> call, Throwable t) {

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
        call.enqueue(new Callback<RestBody<User>>() {
            @Override
            public void onResponse(Call<RestBody<User>> call, Response<RestBody<User>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError(response.body().error);
                    return;
                }
                callback.onSuccess(response.body().response.getId());
            }

            @Override
            public void onFailure(Call<RestBody<User>> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError(new Error(-1, "No se pudo recuperar la cuenta"));
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
        call.enqueue(new Callback<RestBody<Boolean>>() {
            @Override
            public void onResponse(Call<RestBody<Boolean>> call, Response<RestBody<Boolean>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    return;
                }
            }

            @Override
            public void onFailure(Call<RestBody<Boolean>> call, Throwable t) {
            }
        });
    }

    /**
     * Funcion que se encarga de ejecutar la request Oauth de todos los tipos
     * @param call
     * @param callback
     */
    protected void oauthExecuteCall(RestBodyCall<AccessToken> call, final AccessTokenResult callback){
        call.enqueue(new Callback<RestBody<AccessToken>>() {
            @Override
            public void onResponse(Call<RestBody<AccessToken>> call, Response<RestBody<AccessToken>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError(response.body().error);
                    return;
                }
                // Enviamos el accessToken obtenido
                callback.onSuccess(response.body().response.access_token);
            }

            @Override
            public void onFailure(Call<RestBody<AccessToken>> call, Throwable t) {
                callback.onError(new Error(-1, "Inesperado"));
            }
        });
    }

    /**
     * Funcion que se encarga de ejecutar la request para registro
     * @param call
     * @param callback
     */
    protected void registerExecuteCall(RestBodyCall<User> call, final RegisterResult callback){
        call.enqueue(new Callback<RestBody<User>>() {
            @Override
            public void onResponse(Call<RestBody<User>> call, Response<RestBody<User>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError(response.body().error);
                    return;
                }
                callback.onSuccess(response.body().response.getId());
            }

            @Override
            public void onFailure(Call<RestBody<User>> call, Throwable t) {
                callback.onError(new Error(-1, "Inesperado"));
            }
        });
    }

    @Override
    public String getBaseUrl() {
        return "http://authentication.mobileia.com/";
    }
}
