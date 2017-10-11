package com.mobileia.authentication;

import android.app.Activity;
import android.support.annotation.Keep;

import com.mobileia.authentication.listener.AccessTokenResult;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;
import com.mobileia.facebook.builder.LoginBuilder;
import com.mobileia.facebook.entity.Profile;
import com.mobileia.facebook.listener.OnErrorLogin;
import com.mobileia.facebook.listener.OnSuccessLogin;

import java.util.Collection;

/**
 * Created by matiascamiletti on 7/8/17.
 */
@Keep
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
     * Almacenamos los permisos para pedir en Facebook
     */
    protected Collection<String> mPermissions = LoginBuilder.PERMISSIONS_WITH_INFO;

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
     * Setea los permisos que se requieren
     * @param permissions
     */
    public void setPermissions(Collection<String> permissions){
        this.mPermissions = permissions;
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
                    mCallback.onError(error);
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
            public void onError(Error error) {
                // No se pudo registrar llamos al callback
                mCallback.onError(error);
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
                .withPermissions(mPermissions)
                .withSuccessResult(new OnSuccessLogin() {
                    @Override
                    public void onSuccess(Profile profile) {
                        // Ya se logueo con Facebook, realizamos petición para generar AccessToken
                        requestAccessToken(profile.id, profile.token);
                    }
                })
                .withErrorResult(new OnErrorLogin() {
                    @Override
                    public void onError(String message) {
                        // Llamamos al callback con error
                        mCallback.onError(new Error(-1, message));
                    }
                })
                .build();
    }
}
