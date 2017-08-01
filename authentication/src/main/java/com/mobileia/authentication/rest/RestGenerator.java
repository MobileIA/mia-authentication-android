package com.mobileia.authentication.rest;

import android.content.Context;
import android.support.v4.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.realm.AuthenticationRealm;
import com.mobileia.core.Mobileia;
import com.mobileia.core.rest.DateDeserializer;

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

public class RestGenerator {
    /**
     * URL de la API
     */
    private static final String API_BASE_URL = "http://auth.mobileia.com/";
    /**
     * Almacena la instancia de Retrofit
     */
    private static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(createConverterFactory())
            .build();


    /**
     * Funcion que se encarga de realizar un login a traves del email y password
     * @param email
     * @param password
     * @param callback
     */
    public static void signIn(String email, String password, final LoginResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Generamos request
        Call<OAuthResponse> call = service.createAccessToken(Mobileia.getInstance().getAppId(), "password", email, password, Mobileia.getInstance().getDeviceToken(), Mobileia.getInstance().getDeviceName(), 0, Locale.getDefault().getLanguage(), BuildConfig.VERSION_NAME);
        call.enqueue(new Callback<OAuthResponse>() {
            @Override
            public void onResponse(Call<OAuthResponse> call, Response<OAuthResponse> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful()) {
                    callback.onError();
                    return;
                }
                // Pedimos la información del usuario
                me(response.body().access_token, callback);
            }

            @Override
            public void onFailure(Call<OAuthResponse> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError();
            }
        });
    }

    public static void me(final String accessToken, final LoginResult callback){
        // Creamos el servicio
        AuthService service = createService(AuthService.class);
        // Configuramos parametros
        JsonObject params = new JsonObject();
        params.addProperty("app_id", Mobileia.getInstance().getAppId());
        params.addProperty("access_token", accessToken);
        // generamos Request
        Call<User> call = service.me(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful()) {
                    callback.onError();
                    return;
                }
                // Obtenemos usuario
                User user = response.body();
                // Guardamos el AccessToken
                user.setAccessToken(accessToken);
                // Guardamos usuario en la DB
                AuthenticationRealm.getInstance().save(user);
                // Llamamos al callback con exito
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError();
            }
        });
    }
    /**
     * Funcion que se encarga de crear el convertor para parsear las fechas en las peticiones
     * @return
     */
    public static GsonConverterFactory createConverterFactory(){
        Gson gson = new GsonBuilder()
                //.setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Date.class, new DateDeserializer()).create();
        return GsonConverterFactory.create(gson);
    }
    /**
     * Crea el servicio de Retrofit
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        return sRetrofit.create(serviceClass);
    }
}
