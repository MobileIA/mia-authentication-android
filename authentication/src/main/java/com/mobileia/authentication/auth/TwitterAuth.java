package com.mobileia.authentication.auth;

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;

import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;
import com.mobileia.twitter.builder.TwitterLoginBuilder;
import com.mobileia.twitter.entity.TwitterUser;
import com.mobileia.twitter.listener.OnTwitterLoginResult;

/**
 * Created by matiascamiletti on 9/8/17.
 */

public class TwitterAuth extends AuthBase {
    /**
     * Almacena el token del usuario
     */
    protected String mTwitterToken;
    /**
     * Almacena el secret del usuario
     */
    protected String mTwitterSecret;

    /**
     * Constructor
     *
     * @param activity
     */
    public TwitterAuth(Activity activity) {
        super(activity);
    }

    /**
     * Se encarga de pedir un AccessToken valido para consultar los webservices posteriormente
     */
    @Override
    public void requestAccessToken() {
        new RestGenerator().oauthTwitter(mTwitterToken, mTwitterSecret, mAccessTokenResult);
    }

    /**
     * Funcion que crea una nueva cuenta con los datos correctos de twitter
     */
    @Override
    public void requestNewAccount() {
        new RestGenerator().registerWithTwitter(mTwitterToken, mTwitterSecret, mRegisterResult);
    }

    @Override
    public void openSocial() {
        new TwitterLoginBuilder()
                .withActivity(mActivity)
                .withResult(new OnTwitterLoginResult() {
                    @Override
                    public void onSuccess(TwitterUser user) {
                        // Guardamos datos del token
                        mTwitterToken = user.token;
                        mTwitterSecret = user.secret;
                        System.out.println("Twitter + " + mTwitterToken + " -  " +mTwitterSecret);
                        // Ya se logueo con Twitter, realizamos petición para generar AccessToken
                        requestAccessToken();
                    }

                    @Override
                    public void onError() {
                        // Llamamos al callback con error
                        mCallback.onError(new Error(-1, "No se pudo loguear con la cuenta de twitter."));
                    }
                })
                .build();
    }
}
