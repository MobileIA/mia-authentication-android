package com.mobileia.authentication.auth;

import android.app.Activity;

import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 9/8/17.
 */

abstract public class AuthBase implements AuthInterface {
    /**
     * Almacena la actividad
     */
    protected Activity mActivity;
    /**
     * Almacenamos el callback
     */
    protected LoginResult mCallback;
    /**
     * Almacena el AccessToken generado del usuario
     */
    protected String mAccessToken;
    /**
     * Callback al generar AccessToken, en todas las redes es identico
     */
    protected AccessTokenResult mAccessTokenResult = new AccessTokenResult() {
        @Override
        public void onSuccess(String accessToken) {
            // Guardar AccessToken
            mAccessToken = accessToken;
            // realizamos petición del perfil del usuario
            requestProfile();
        }

        @Override
        public void onError(Error error) {
            System.out.println("Twitter error + " + error.message);
            // Verificamos si no se pudo loguear porque la cuenta no existe
            if(error.code == 414){
                // Creamos una nueva cuenta ya que es la primera vez que se loguea
                requestNewAccount();
            }else{
                // No se pudo loguear por otro motivo
                mCallback.onError(new Error(-1, "No se pudo obtener el access_token"));
            }
        }
    };
    /**
     * Callback que se usa al registrar la cuenta
     */
    protected RegisterResult mRegisterResult = new RegisterResult() {
        @Override
        public void onSuccess(int userId) {
            // Al registrar una nueva cuenta realizamos el login por default
            requestAccessToken();
        }

        @Override
        public void onError(Error error) {
            // No se pudo registrar llamos al callback
            mCallback.onError(new Error(-1, "No se pudo registrar la cuenta"));
        }
    };

    /**
     * Constructor
     * @param activity
     */
    public AuthBase(Activity activity){
        mActivity = activity;
    }

    /**
     * Funcion que se encarga de realizar el login con Facebook
     * @param callback
     */
    public void signIn(LoginResult callback){
        // Guardamos el callback
        mCallback = callback;
        // Abrimos red social
        openSocial();
    }

    /**
     * Se encarga de pedir la información del usuario asi la guardamos
     */
    protected void requestProfile(){
        new RestGenerator().me(mAccessToken, mCallback);
    }
}
