package com.mobileia.authentication.core;

import android.app.Activity;

import com.mobileia.authentication.core.listener.AccessTokenResult;
import com.mobileia.authentication.core.listener.LoginResult;
import com.mobileia.authentication.core.listener.RegisterResult;
import com.mobileia.authentication.core.rest.AuthRestBase;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/1/18.
 */

abstract public class MobileiaAuthBase implements MobileiaAuthInterface {
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
            // realizamos peticion del perfil del usuario
            requestProfile();
        }

        @Override
        public void onError(Error error) {
            // Verificamos si no se pudo loguear porque la cuenta no existe
            if(error.code == 414){
                // Creamos una nueva cuenta ya que es la primera vez que se loguea
                requestNewAccount();
            }else{
                // No se pudo loguear por otro motivo
                mCallback.onError(error);
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
            mCallback.onError(error);
        }
    };

    /**
     * Constructor
     * @param activity
     */
    public MobileiaAuthBase(Activity activity){
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
     * Se encarga de pedir la informacion del usuario asi la guardamos
     */
    protected void requestProfile(){
        new AuthRestBase().me(mAccessToken, mCallback);
    }

    /**
     * Setea el callback
     * @param callback
     */
    public void setCallback(LoginResult callback){
        this.mCallback = callback;
    }

    /**
     * Obtiene el callback
     * @return
     */
    public LoginResult getCallback(){ return this.mCallback; }
}
