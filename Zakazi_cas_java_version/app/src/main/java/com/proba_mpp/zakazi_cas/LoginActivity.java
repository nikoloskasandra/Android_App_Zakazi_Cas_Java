package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    
    //binding
    private ActivityLoginBinding binding;
    
    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        
        //setup progress dialog
        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Ве молиме почекајте");
        progressDialog.setCanceledOnTouchOutside(false);


        //back to Login -- might change later to main
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //go to Register
        binding.noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        
        //loginbtn 
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  loginUser();
            }
        });

    }

    String email = "", password = "";

    private void loginUser() {
        progressDialog.setMessage("Најава на корисник ..");
        progressDialog.show();


        //validation
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if(email.isEmpty()){
            binding.emailEt.setError("Празно поле");
            binding.emailEt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Внесете вистинска емаил адреса !");
            binding.emailEt.requestFocus();
            return;
        }
        if(password.isEmpty()){
            binding.passwordEt.setError("Празно поле");
            binding.passwordEt.requestFocus();
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Успешна најава.",Toast.LENGTH_SHORT).show();
                            // navigate to User Profile or Admindashboard
                            progressDialog.dismiss();

                            if(email.contains("admin") || email.contains("ADMIN") || email.contains("Admin")){
                                startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                finish();
                            }



                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Најавата не е успешна, пробај повторно.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    @Override
    protected void onStart() {
        super.onStart();

        if(checkIfLoggedIn()){
            String email  = firebaseAuth.getCurrentUser().getEmail().toString().trim();
            if(email.contains("admin") || email.contains("ADMIN") || email.contains("Admin")){
                startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                finish();
            }
        }
    }

    private boolean checkIfLoggedIn() {
        return firebaseAuth.getCurrentUser()!=null;
    }


}