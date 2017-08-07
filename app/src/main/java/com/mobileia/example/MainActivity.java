package com.mobileia.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobileia.authentication.MobileiaAuth;
import com.mobileia.authentication.entity.User;
import com.mobileia.authentication.listener.LoginResult;
import com.mobileia.authentication.listener.RegisterResult;
import com.mobileia.core.Mobileia;
import com.mobileia.core.entity.Error;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Configurar Mobileia Lab
        Mobileia.getInstance().setAppId(8);
        // Configurar token del dispositivo
        Mobileia.getInstance().setDeviceToken(FirebaseInstanceId.getInstance().getToken());
        // Verificar si ya esta logueado
        /*User currentUser = MobileiaAuth.getInstance(this).getCurrentUser();
        if(currentUser == null){
            onClickLogin(null);
            return;
        }
        // Mostrar datos del usuario
        System.out.println("MIA Auth: Logueado: " + currentUser.getFirstname());
        System.out.println("MIA Auth: Logueado: " + currentUser.getId());
        System.out.println("MIA Auth: Logueado: " + currentUser.getEmail());
        System.out.println("MIA Auth: Logueado: " + currentUser);*/
    }

    public void onClickLogin(View v){
        // Login de Usuario
        MobileiaAuth.getInstance(this).signInWithEmailAndPassword("matiascamiletti@mobileia.com", "123Qwerty", new LoginResult() {
            @Override
            public void onSuccess(User user) {
                System.out.println("MIA Auth: Success");
                System.out.println("MIA Auth: " + user.getId());
                System.out.println("MIA Auth: " + user.getAppId());
                System.out.println("MIA Auth: " + user.getFirstname());
                System.out.println("MIA Auth: " + user.getLastname());
                System.out.println("MIA Auth: " + user.getEmail());
                System.out.println("MIA Auth: " + user.getAccessToken());
                System.out.println("MIA Auth: " + user.getPhoto());
                System.out.println("MIA Auth: " + user.getCreatedAt());
            }

            @Override
            public void onError(Error error) {
                System.out.println("MIA Auth: Error");
            }
        });
    }

    public void onClickFacebook(View v){
        MobileiaAuth.getInstance(this).signInWithFacebook(this, new LoginResult() {
            @Override
            public void onSuccess(User user) {
                System.out.println("MIA Auth: Success Facebook");
                System.out.println("MIA Auth: " + user.getId());
                System.out.println("MIA Auth: " + user.getFirstname());
                System.out.println("MIA Auth: " + user.getLastname());
                System.out.println("MIA Auth: " + user.getEmail());
                System.out.println("MIA Auth: " + user.getAccessToken());
                System.out.println("MIA Auth: " + user.getPhoto());
                System.out.println("MIA Auth: " + user.getCreatedAt());
            }

            @Override
            public void onError(Error error) {
                System.out.println("MIA Auth: Error Facebook: " + error.message);
            }
        });
    }

    public void onClickRegister(View v){
        // Creamos el objeto Usuario con los datos
        final User user = new User();
        user.setFirstname("Mati25");
        user.setEmail("mati25435354@gmail.com");
        user.setLastname("Cami25");
        // Llamamos al servicio para registrar cuenta
        MobileiaAuth.getInstance(this).createAccount(user, "123456Password", new RegisterResult() {
            @Override
            public void onSuccess(int userId) {
                System.out.println("MIA Auth: Success");
                System.out.println("MIA Auth: " + user.getId());
                System.out.println("MIA Auth: " + user.getFirstname());
                System.out.println("MIA Auth: " + user.getLastname());
                System.out.println("MIA Auth: " + user.getEmail());
                System.out.println("MIA Auth: " + user.getAccessToken());
                System.out.println("MIA Auth: " + user.getPhoto());
                System.out.println("MIA Auth: " + user.getCreatedAt());
            }

            @Override
            public void onError() {
                System.out.println("MIA Auth: Error");
            }
        });
    }
}
