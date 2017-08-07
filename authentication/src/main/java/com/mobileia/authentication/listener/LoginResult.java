package com.mobileia.authentication.listener;

import com.mobileia.authentication.entity.User;
import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public interface LoginResult {
    void onSuccess(User user);
    void onError(Error error);
}
