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
import com.mobileia.core.rest.RestBodyCallback;
import com.mobileia.core.rest.RestBuilder;

import java.util.Locale;

import retrofit2.Call;
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
     * Funcion que se encarga de obtener los datos del usuario a traves del AccessToken
     * @param accessToken
     * @param callback
     */
    public void me(final String accessToken, final LoginResult callback){
        // Creamos el servicio
        UserService service = createService(UserService.class);
        // generamos Request
        RestBodyCall<User> call = service.me(Mobileia.getInstance().getAppId(), accessToken);
        // Ejecutamos call
        call.enqueue(new RestBodyCallback<User>() {
            @Override
            public void onSuccess(User body) {
                // Obtenemos usuario
                User user = body;
                // Guardamos el AccessToken
                user.setAccessToken(accessToken);
                // Guardamos usuario en la DB
                AuthBaseRealm.getInstance().save(user);
                // Llamamos al callback con exito
                callback.onSuccess(user);
            }

            @Override
            public void onError(Error error) {
                // Llamamos al callback porque hubo error
                callback.onError(error);
            }
        });
    }
    /**
     * Funcion que devuelve la URL base de MobileIA Authentication
     * @return
     */
    @Override
    public String getBaseUrl() {
        return "https://authentication.mobileia.com/";
    }
}
