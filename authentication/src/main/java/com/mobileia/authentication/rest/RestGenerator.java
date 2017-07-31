package com.mobileia.authentication.rest;

import android.support.v4.BuildConfig;

import com.mobileia.authentication.entity.OAuth;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.core.Mobileia;

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
            .addConverterFactory(GsonConverterFactory.create())
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
        Call<OAuth> call = service.createAccessToken(Mobileia.getInstance().getAppId(), "password", email, password, Mobileia.getInstance().getDeviceToken(), Mobileia.getInstance().getDeviceName(), 0, Locale.getDefault().getLanguage(), BuildConfig.VERSION_NAME);
        call.enqueue(new Callback<OAuth>() {
            @Override
            public void onResponse(Call<OAuth> call, Response<OAuth> response) {
                // Verificar si la respuesta fue incorrecta
                if (!response.isSuccessful()) {
                    callback.onError();
                    return;
                }
                // Llamamos al callback con exito
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<OAuth> call, Throwable t) {
                // Llamamos al callback porque hubo error
                callback.onError();
            }
        });
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
