package com.mobileia.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobileia.authentication.MobileiaAuth;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.core.Mobileia;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Configurar Mobileia Lab
        Mobileia.getInstance().setAppId(4);
        // Configurar token del dispositivo
        Mobileia.getInstance().setDeviceToken(FirebaseInstanceId.getInstance().getToken());
        // Login de Usuario
        MobileiaAuth.getInstance().signInWithEmailAndPassword("matiascamiletti@mobileia.com", "123456", new LoginResult() {
            @Override
            public void onSuccess() {
                System.out.println("MIA Auth: Success");
            }

            @Override
            public void onError() {
                System.out.println("MIA Auth: Error");
            }
        });
    }
}
