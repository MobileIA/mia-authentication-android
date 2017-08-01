package com.mobileia.authentication;

import android.content.Context;

import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.realm.AuthenticationRealm;
import com.mobileia.authentication.rest.RestGenerator;

import io.realm.Realm;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class MobileiaAuth {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static MobileiaAuth sOurInstance = null;
    /**
     * Almacena el contexto
     */
    protected Context mContext;

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static MobileiaAuth getInstance(Context context) {
        if(sOurInstance == null){
            sOurInstance = new MobileiaAuth(context.getApplicationContext());
        }
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

    /**
     * Devuelve el usuario logueado si existe
     * @return
     */
    public User getCurrentUser(){
        return AuthenticationRealm.getInstance().fetchUser();
    }

    /**
     * Constructor del singleton
     * @param context
     */
    private MobileiaAuth(Context context) {
        // Guardamos contexto
        this.mContext = context;
        // Iniciamos Realm
        Realm.init(context);
    }
}
