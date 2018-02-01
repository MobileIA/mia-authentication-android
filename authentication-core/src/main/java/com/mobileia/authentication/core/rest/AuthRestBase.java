package com.mobileia.authentication.core.rest;

import com.google.gson.JsonObject;
import com.mobileia.authentication.core.entity.AccessToken;
import com.mobileia.authentication.core.entity.User;
import com.mobileia.authentication.core.listener.AccessTokenResult;
import com.mobileia.authentication.core.listener.LoginResult;
import com.mobileia.authentication.core.listener.RegisterResult;
import com.mobileia.authentication.core.realm.AuthBaseRealm;
import com.mobileia.authentication.core.rest.service.UserService;
import com.mobileia.core.Mobileia;
import com.mobileia.core.entity.Error;
import com.mobileia.core.rest.RestBody;
import com.mobileia.core.rest.RestBodyCall;
import com.mobileia.core.rest.RestBuilder;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by matiascamiletti on 31/1/18.
 */

public class AuthRestBase extends RestBuilder {

    /**
     * Hace un request para registrar un usuario con los datos ingresados
     * @param params
     * @param callback
     */
    public void register(JsonObject params, final RegisterResult callback){
        // Creamos el servicio
        UserService service = createService(UserService.class);
        // Configuramos parametros
        params.addProperty("app_id", Mobileia.getInstance().getAppId());
        params.addProperty("device_token", Mobileia.getInstance().getDeviceToken());
        params.addProperty("device_model", Mobileia.getInstance().getDeviceName());
        params.addProperty("platform", "0");
        params.addProperty("language", Locale.getDefault().getLanguage());
        params.addProperty("version", "1.0");
        // Generamos Request
        RestBodyCall<User> call = service.register(params);
        // Ejecutamos la request
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
    /**
     * Funcion que se encarga de pedir el accessToken del usuario
     * @param params
     * @param callback
     */
    public void oauth(JsonObject params, final AccessTokenResult callback){
        // Creamos el servicio
        UserService service = createService(UserService.class);
        // Configuramos parametros
        params.addProperty("app_id", Mobileia.getInstance().getAppId());
        params.addProperty("device_token", Mobileia.getInstance().getDeviceToken());
        params.addProperty("device_model", Mobileia.getInstance().getDeviceName());
        params.addProperty("platform", "0");
        params.addProperty("language", Locale.getDefault().getLanguage());
        params.addProperty("version", "1.0");
        // Generamos request
        RestBodyCall<AccessToken> call = service.oauth(params);
        // Ejecutamos request
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
     * Funcion que se encarga de obtener los datos del usuario a traves del AccessToken
     * @param accessToken
     * @param callback
     */
    public void me(final String accessToken, final LoginResult callback){
        // Creamos el servicio
        UserService service = createService(UserService.class);
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
                AuthBaseRealm.getInstance().save(user);
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
     * Funcion que devuelve la URL base de MobileIA Authentication
     * @return
     */
    @Override
    public String getBaseUrl() {
        return "http://authentication.mobileia.com/";
    }
}
