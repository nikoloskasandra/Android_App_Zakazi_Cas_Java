package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proba_mpp.zakazi_cas.Model.User;
import com.proba_mpp.zakazi_cas.databinding.ActivityLoginBinding;
import com.proba_mpp.zakazi_cas.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    //binding
    private ActivityRegisterBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Ве молиме почекајте.");
        progressDialog.setCanceledOnTouchOutside(false);



        //back to Login -- might change later to main
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // handle click,  registration
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    String fullname = "", age ="", phone= "", location ="", about="", email ="", password="";
    String image ="";
    private void validateData() {
        //Before creating an account, lets check data validation.

        //get data
        fullname = binding.nameEt.getText().toString().trim();
        age = binding.ageEt.getText().toString().trim();
        phone  = binding.phoneEt.getText().toString().trim();
        location = binding.locationEt.getText().toString().trim();
        about = binding.descriptionEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        image = "";
        String confirm_password = binding.cPasswordEt.getText().toString().trim();

        //validate data
        if(TextUtils.isEmpty(fullname)){

            Toast.makeText(this, "Внесете име и презиме..", Toast.LENGTH_SHORT).show();
            binding.nameEt.setText("Празно поле !");
            binding.nameEt.requestFocus();

        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            Toast.makeText(this, "Не валидна е-маил адреса.", Toast.LENGTH_SHORT).show();
            binding.emailEt.setText("Празно поле !");
            binding.emailEt.requestFocus();

        }else if (TextUtils.isEmpty(password)){

            Toast.makeText(this, "Внесете лозинка...", Toast.LENGTH_SHORT).show();
            binding.passwordEt.setText("Празно поле !");
            binding.passwordEt.requestFocus();

        }else if(TextUtils.isEmpty(confirm_password)){

            binding.cPasswordEt.setText("Празно поле !");
            binding.cPasswordEt.requestFocus();
            Toast.makeText(this, "Потврдете ја лозинката...",Toast.LENGTH_SHORT).show();

        }else if (!password.equals(confirm_password)){

            binding.cPasswordEt.setText("Празно поле !");
            binding.cPasswordEt.requestFocus();
            Toast.makeText(this, "Лозинките не се се цовпаѓаат...",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(age)){

            binding.ageEt.setText("Празно поле !");
            binding.ageEt.requestFocus();
            Toast.makeText(this, "Внесете ги вашите години",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){

            binding.phoneEt.setText("Празно поле !");
            binding.phoneEt.requestFocus();
            Toast.makeText(this, "Внесете го вашиот телефонски број..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(location)){

            binding.locationEt.setText("Празно поле !");
            binding.locationEt.requestFocus();
            Toast.makeText(this, "Внесете го вашето месно на живеење...",Toast.LENGTH_SHORT).show();
        }
        else{
            createUserAccount();
        }
    }

    private void createUserAccount() {
        //show progress dialog
        progressDialog.setMessage("Сметката на корисникот е во процес на креирање..");
        progressDialog.show();

        //Database ref (Users)
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        //create user in Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //create new user to add in real database
                            User user = new User(fullname, age, phone, email, image="", about,location);
                            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                       progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Корисникот е регистриран!", Toast.LENGTH_SHORT).show();

                                        binding.nameEt.setText("");
                                        binding.ageEt.setText("");
                                        binding.phoneEt.setText("");
                                        binding.locationEt.setText("");
                                        binding.descriptionEt.setText("");
                                        binding.emailEt.setText("");
                                        binding.passwordEt.setText("");
                                        binding.cPasswordEt.setText("");

                                        //startActivity(new Intent(RegisterActivity.this, ));
                                        //ОТКАКО ЌЕ СЕ РЕГИСТРИРА КАДЕ ДА СЕ ПРЕНАСОЧИМЕ  --> User Profile
                                        if(email.contains("admin") || email.contains("ADMIN") || email.contains("Admin")){
                                            startActivity(new Intent(RegisterActivity.this, DashboardAdminActivity.class));
                                            finish();
                                        }else {
                                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                                            finish(); //се со цел да не може да се вратиме на login
                                        }


                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Пробајте повторно!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                   progressDialog.dismiss();
                    }
                });

    }

}