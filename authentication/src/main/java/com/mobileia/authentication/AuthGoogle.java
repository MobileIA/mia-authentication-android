package com.mobileia.authentication;

import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;
import com.mobileia.google.MobileiaGoogle;
import com.mobileia.google.builder.GoogleLoginBuilder;
import com.mobileia.google.listener.OnErrorGoogleLogin;
import com.mobileia.google.listener.OnSuccessGoogleLogin;

/**
 * Created by matiascamiletti on 8/8/17.
 */
@Keep
public class AuthGoogle {
    /**
     * Almacena la actividad
     */
    protected FragmentActivity mActivity;
    /**
     * Almacenamos el callback
     */
    protected LoginResult mCallback;
    /**
     * Almacena el ID de google
     */
    protected String mGoogleId;
    /**
     * Almacena instancia de MobileiaGoogle
     */
    protected MobileiaGoogle mMobileiaGoogle;

    /**
     * Constructor
     * @param activity
     */
    public AuthGoogle(FragmentActivity activity){
        mActivity = activity;
    }

    /**
     * Funcion que se encarga de realizar el login con Facebook
     * @param callback
     */
    public void signIn(String googleId, LoginResult callback){
        // Guardamos el callback
        mCallback = callback;
        // Guardamos ID de google
        mGoogleId = googleId;
        // Abrimos Google
        openGoogle();
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mMobileiaGoogle != null){
            mMobileiaGoogle.onActivityResult(requestCode, resultCode, data);
        }
    }
    /**
     * Se encarga de pedir la información del usuario asi la guardamos
     * @param accessToken
     */
    protected void requestProfile(String accessToken){
        new RestGenerator().me(accessToken, mCallback);
    }
    /**
     * Se encarga de pedir un AccessToken valido para consultar los webservices posteriormente
     * @param googleToken
     */
    protected void requestAccessToken(final String googleToken){
        new RestGenerator().oauthGoogle(googleToken, new AccessTokenResult() {
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
                    createNewAccount(googleToken);
                }else{
                    System.out.println("GoogleMiA : error : " + error.message + " " + error.code);
                    // No se pudo loguear por otro motivo
                    mCallback.onError(error);
                }
            }
        });
    }

    /**
     * Funcion que crea una nueva cuenta con los datos correctos de google
     * @param googleToken
     */
    protected void createNewAccount(final String googleToken){
        new RestGenerator().registerWithGoogle(googleToken, new RegisterResult() {
            @Override
            public void onSuccess(int userId) {
                // Al registrar una nueva cuenta realizamos el login por default
                requestAccessToken(googleToken);
            }

            @Override
            public void onError(Error error) {
                // No se pudo registrar llamos al callback
                mCallback.onError(error);
            }
        });
    }
    /**
     * Llama a la libreria de Facebook y se encarga de realizar el login
     */
    protected void openGoogle(){
        mMobileiaGoogle = new GoogleLoginBuilder()
                .withActivity(mActivity)
                .withGoogleId(mGoogleId)
                .withSuccessResult(new OnSuccessGoogleLogin() {
                    @Override
                    public void onSuccess(GoogleSignInAccount account) {
                        // Ya se logueo con Facebook, realizamos petición para generar AccessToken
                        requestAccessToken(account.getIdToken());
                    }
                })
                .withErrorResult(new OnErrorGoogleLogin() {
                    @Override
                    public void onError(int code, String message) {
                        // Llamamos al callback con error
                        mCallback.onError(new Error(code, message));
                    }
                })
                .build();
        mMobileiaGoogle.login();
    }
}
