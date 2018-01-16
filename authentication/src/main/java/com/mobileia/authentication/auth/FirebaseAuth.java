package com.mobileia.authentication.auth;

import android.app.Activity;

import com.mobileia.authentication.rest.RestGenerator;
import com.mobileia.core.entity.Error;
import com.mobileia.firebase.builder.LoginPhoneBuilder;
import com.mobileia.firebase.listener.OnErrorLogin;
import com.mobileia.firebase.listener.OnSuccessLogin;

/**
 * Created by matiascamiletti on 10/1/18.
 */

public class FirebaseAuth extends AuthBase {
    /**
     * Almacena el telefono del usuario
     */
    protected String mPhone;
    /**
     * Almacena el secret del usuario
     */
    protected String mToken;
    /**
     * Constructor
     *
     * @param activity
     */
    public FirebaseAuth(Activity activity) {
        super(activity);
    }

    @Override
    public void requestAccessToken() {
        new RestGenerator().oauthPhone(mPhone, mToken, mAccessTokenResult);
    }

    @Override
    public void requestNewAccount() {
        new RestGenerator().registerWithPhone(mPhone, mToken, mRegisterResult);
    }

    @Override
    public void openSocial() {
        new LoginPhoneBuilder()
                .withActivity(mActivity)
                .withSuccessResult(new OnSuccessLogin() {
                    @Override
                    public void onSuccess(com.firebase.ui.auth.IdpResponse profile) {
                        // Guardamos datos del usuario
                        mPhone = profile.getPhoneNumber();
                        mToken = profile.getIdpToken();
                        // Pedimos los datos
                        requestAccessToken();
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
