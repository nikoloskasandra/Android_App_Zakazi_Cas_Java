package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proba_mpp.zakazi_cas.Model.Category;
import com.proba_mpp.zakazi_cas.databinding.ActivityAddCategoryBinding;

public class AddCategoryActivity extends AppCompatActivity {

    //binding
    private ActivityAddCategoryBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //progressdialog configuration
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Ве молиме почекајте");
        progressDialog.setCanceledOnTouchOutside(false);

        //back button
         binding.backBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onBackPressed();
             }
         });

         //submit category button
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String category = "";

    private void validateData() {
        //before adding validate data

        //get data
        category = binding.categoryEt.getText().toString().trim();
        //validate if not empty
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Ве молиме внесете категорија ..", Toast.LENGTH_SHORT).show();
        }else {
            addCategoryFirebase();
        }

    }

    private void addCategoryFirebase() {
        //show progress dialog
        progressDialog.setMessage("Додавање на категорија..");
        progressDialog.show();


        Category newCategory = new Category(category);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+category)
                .setValue(newCategory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //category add success
                        binding.categoryEt.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(AddCategoryActivity.this, "Категоријата е успешно додадена..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCategoryActivity.this, DashboardAdminActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed
                        progressDialog.dismiss();
                        Toast.makeText(AddCategoryActivity.this, "Настана грешка при додавање на категоријата..", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}