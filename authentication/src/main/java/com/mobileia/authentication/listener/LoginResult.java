package com.mobileia.authentication.listener;

import android.support.annotation.Keep;

import com.mobileia.authentication.entity.User;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/7/17.
 */
@Keep
public interface LoginResult {
    void onSuccess(User user);
    void onError(Error error);
}
