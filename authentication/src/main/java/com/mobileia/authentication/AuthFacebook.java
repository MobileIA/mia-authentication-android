package com.mobileia.authentication;

import android.app.Activity;

import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;
import com.mobileia.facebook.builder.LoginBuilder;
import com.mobileia.facebook.entity.Profile;
import com.mobileia.facebook.listener.OnErrorLogin;
import com.mobileia.facebook.listener.OnSuccessLogin;

/**
 * Created by matiascamiletti on 7/8/17.
 */

public class AuthFacebook {
    /**
     * Almacena la actividad
     */
    protected Activity mActivity;
    /**
     * Almacenamos el callback
     */
    protected LoginResult mCallback;

    /**
     * Constructor
     * @param activity
     */
    public AuthFacebook(Activity activity){
        mActivity = activity;
    }

    /**
     * Funcion que se encarga de realizar el login con Facebook
     * @param callback
     */
    public void signIn(LoginResult callback){
        // Guardamos el callback
        mCallback = callback;
        // Abrimos facebook
        openFacebook();
    }

    /**
     * Se encarga de pedir un AccessToken valido para consultar los webservices posteriormente
     * @param facebookId
     * @param facebookAccessToken
     */
    protected void requestAccessToken(final String facebookId, final String facebookAccessToken){
        new RestGenerator().oauthFacebook(facebookId, facebookAccessToken, new AccessTokenResult() {
            @Override
            public void onSuccess(String accessToken) {
                // realizamos petición del perfil del usuario
                requestProfile(accessToken);
            }

            @Override
            public void onError(Error error) {
                // Verificamos si no se pudo loguear porque la cuenta no existe
                if(error.code == 414){
                    // Creamos una nueva cuenta ya que es la primera vez que se loguea
                    createNewAccount(facebookId, facebookAccessToken);
                }else{
                    // No se pudo loguear por otro motivo
                    mCallback.onError(new Error(-1, "No se pudo obtener el access_token"));
                }
            }
        });
    }

    /**
     * Funcion que crea una nueva cuenta con los datos correctos de facebook
     * @param facebookId
     * @param facebookAccessToken
     */
    protected void createNewAccount(final String facebookId, final String facebookAccessToken){
        new RestGenerator().registerWithFacebook(facebookId, facebookAccessToken, new RegisterResult() {
            @Override
            public void onSuccess(int userId) {
                // Al registrar una nueva cuenta realizamos el login por default
                requestAccessToken(facebookId, facebookAccessToken);
            }

            @Override
            public void onError() {
                // No se pudo registrar llamos al callback
                mCallback.onError(new Error(-1, "No se pudo registrar la cuenta de facebook"));
            }
        });
    }

    /**
     * Se encarga de pedir la información del usuario asi la guardamos
     * @param accessToken
     */
    protected void requestProfile(String accessToken){
        new RestGenerator().me(accessToken, mCallback);
    }
    /**
     * Llama a la libreria de Facebook y se encarga de realizar el login
     */
    protected void openFacebook(){
        new LoginBuilder()
                .withActivity(mActivity)
                .withPermissions(LoginBuilder.PERMISSIONS_WITH_INFO)
                .withSuccessResult(new OnSuccessLogin() {
                    @Override
                    public void onSuccess(Profile profile) {
                        // Ya se logueo con Facebook, realizamos petición para generar AccessToken
                        requestAccessToken(profile.id, profile.token);
                    }
                })
                .withErrorResult(new OnErrorLogin() {
                    @Override
                    public void onError() {
                        // Llamamos al callback con error
                        mCallback.onError(new Error(-1, "No se pudo loguear con la cuenta de facebook."));
                    }
                })
                .build();
    }
}
