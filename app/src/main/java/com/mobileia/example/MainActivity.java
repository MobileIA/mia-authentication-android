package com.mobileia.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobileia.authentication.MobileiaAuth;
import com.mobileia.authentication.core.entity.User;
import com.mobileia.authentication.core.listener.LoginResult;
import com.mobileia.authentication.core.listener.RegisterResult;
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
        loadUser();
    }

    public void loadUser(){
        TextView textUser = (TextView)findViewById(R.id.textUser);

        User currentUser = MobileiaAuth.getInstance(this).getCurrentUser();
        if(currentUser != null){
            textUser.setText(currentUser.getFirstname() + " Tocar aqui para desloguear.");
        }else{
            textUser.setText("No hay nadie logueado!");
        }
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

                loadUser();
            }

            @Override
            public void onError(Error error) {
                System.out.println("MIA Auth: Error" + error.message);
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

    public void onClickGoogle(View view){
        /*MobileiaAuth.getInstance(this).signInWithGoogle(this, "428204135163-ak18l1l7sm490d7s6t0d5q4v69m2rruc.apps.googleusercontent.com", new LoginResult() {
            @Override
            public void onSuccess(User user) {
                System.out.println("MIA Auth: Success Google");
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
                System.out.println("MIA Auth: Error Google: " + error.message);
            }
        });*/
    }

    public void onClickTwitter(View v){
        MobileiaAuth.getInstance(this).signInWithTwitter(this, new LoginResult() {
            @Override
            public void onSuccess(User user) {
                System.out.println("MIA Auth: Success Twitter");
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
                System.out.println("MIA Auth: Error Twitter: " + error.message);
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
            public void onError(Error error) {
                System.out.println("MIA Auth: Error");
            }
        });
    }

    public void onClickLogout(View v){
        MobileiaAuth.getInstance(this).logoutUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //MobileiaAuth.getInstance(this).getAuthGoogle().onActivityResult(requestCode, resultCode, data);
    }
}
