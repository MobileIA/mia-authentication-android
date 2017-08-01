package com.mobileia.authentication.listener;

/**
 * Created by matiascamiletti on 1/8/17.
 */

public interface RegisterResult {
    void onSuccess(int userId);
    void onError();
}
