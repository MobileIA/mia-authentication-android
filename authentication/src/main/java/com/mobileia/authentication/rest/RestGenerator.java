package com.mobileia.authentication.rest;

import android.content.Context;
import android.support.v4.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mobileia.authentication.entity.AccessToken;
import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.realm.AuthenticationRealm;
import com.mobileia.core.Mobileia;
import com.mobileia.core.entity.Error;
import com.mobileia.core.rest.DateDeserializer;
import com.mobileia.core.rest.RestBody;
import com.mobileia.core.rest.RestBodyCall;
import com.mobileia.core.rest.RestBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                // Pedimos la información del usuario
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
                // Llamamos al callback porque hubo error
                callback.onError(new Error(-1, "Inesperado"));
            }
        });
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
     * Hace un request para generar un AccessToken desde una cuenta de Google
     * @param googleToken
     * @param callback
     */
    public void oauthGoogle(String googleToken, final AccessTokenResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos request
        RestBodyCall<AccessToken> call = service.oauthWithGoogle(Mobileia.getInstance().getAppId(), "google", googleToken);
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
        call.enqueue(new Callback<RestBody<User>>() {
            @Override
            public void onResponse(Call<RestBody<User>> call, Response<RestBody<User>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError();
                    return;
                }
                callback.onSuccess(response.body().response.getId());
            }

            @Override
            public void onFailure(Call<RestBody<User>> call, Throwable t) {
                callback.onError();
            }
        });
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
        call.enqueue(new Callback<RestBody<User>>() {
            @Override
            public void onResponse(Call<RestBody<User>> call, Response<RestBody<User>> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful() || !response.body().success) {
                    callback.onError();
                    return;
                }
                callback.onSuccess(response.body().response.getId());
            }

            @Override
            public void onFailure(Call<RestBody<User>> call, Throwable t) {
                callback.onError();
            }
        });
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















    public void createAccount(final User user, String password, final RegisterResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Configuramos parametros
        JsonObject params = new JsonObject();
        params.addProperty("app_id", Mobileia.getInstance().getAppId());
        params.addProperty("email", user.getEmail());
        params.addProperty("password", password);
        params.addProperty("firstname", user.getFirstname());
        params.addProperty("lastname", user.getLastname());
        params.addProperty("photo", user.getPhoto());
        params.addProperty("phone", user.getPhone());
        params.addProperty("device_token", Mobileia.getInstance().getDeviceToken());
        params.addProperty("device_model", Mobileia.getInstance().getDeviceName());
        params.addProperty("platform", 0);
        params.addProperty("language", Locale.getDefault().getLanguage());
        params.addProperty("version", BuildConfig.VERSION_NAME);
        // generamos Request
        Call<User> call = service.createAccount(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful()) {
                    callback.onError();
                    return;
                }
                // Llamamos al callback con exito
                callback.onSuccess(response.body().getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError();
            }
        });
    }

    @Override
    public String getBaseUrl() {
        return "http://authentication.mobileia.com/";
    }
}
