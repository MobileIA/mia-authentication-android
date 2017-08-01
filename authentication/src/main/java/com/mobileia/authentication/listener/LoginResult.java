package com.mobileia.authentication.listener;

import com.mobileia.authentication.entity.User;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public interface LoginResult {
    void onSuccess(User user);
    void onError();
}
