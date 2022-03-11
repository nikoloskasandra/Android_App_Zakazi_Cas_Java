package com.proba_mpp.zakazi_cas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.proba_mpp.zakazi_cas.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //in this class i will use binding activity, not findViewById()
    private ActivityMainBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        //handle click on Button Professor „ПРОФЕСОР“
        binding.btnProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //where to ----? add code --- LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });

        //handle click on Button „ПОЧЕТНА“
        binding.btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // where to -----? add code --> may change later
                startActivity(new Intent(MainActivity.this, DashboardUserActivity.class));

            }
        });

        //Create Ad Button
        binding.btnAdAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }

    private void checkUser() {
        //check if user is logged in
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null ){
            startActivity(new Intent(MainActivity.this, AdActivity.class));
        }else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}