package com.mobileia.authentication.core.listener;

import android.support.annotation.Keep;

import com.mobileia.authentication.core.entity.User;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/1/18.
 */

@Keep
public interface LoginResult {
    void onSuccess(User user);
    void onError(Error error);
}
