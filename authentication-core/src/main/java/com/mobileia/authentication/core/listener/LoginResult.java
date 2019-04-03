package com.mobileia.authentication.core.listener;

import com.mobileia.authentication.core.entity.User;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/1/18.
 */

public interface LoginResult {
    void onSuccess(User user);
    void onError(Error error);
}
