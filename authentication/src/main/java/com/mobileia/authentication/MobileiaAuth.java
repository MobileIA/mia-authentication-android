package com.mobileia.authentication;

import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.rest.RestGenerator;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class MobileiaAuth {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static final MobileiaAuth sOurInstance = new MobileiaAuth();

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static MobileiaAuth getInstance() {
        return sOurInstance;
    }

    /**
     * Funcion para realizar un login con email y password
     * @param email
     * @param password
     * @param callback
     */
    public void signInWithEmailAndPassword(String email, String password, LoginResult callback){
        RestGenerator.signIn(email, password, callback);
    }

    private MobileiaAuth() {
    }
}
