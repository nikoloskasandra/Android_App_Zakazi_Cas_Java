package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.Model.Ad;
import com.proba_mpp.zakazi_cas.databinding.ActivityAdBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdActivity extends AppCompatActivity {

    //binding
    private ActivityAdBinding binding;

    //firebaseAuth
    private FirebaseAuth firebaseAuth;

    //array list of categories
    private ArrayList<String> categoryTitleArrayList, categoryIdArrayList;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadCategories();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Ве молиме почекајте");
        progressDialog.setCanceledOnTouchOutside(false);

        //init firebaseauth
        firebaseAuth = FirebaseAuth.getInstance();

        //back button
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //picking category -> when you click on category dialog will be showed
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });

        //Submit Ad
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        //почетна
        binding.btnPocetna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdActivity.this, DashboardUserActivity.class));
            }
        });

    }

    private void loadCategories() {

        categoryTitleArrayList = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();

        //database ref to load categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryIdArrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){

                    //get id and title of the caategory
                    String categoryId = ""+ds.child("category").getValue();
                    String categoryTitle = ""+ds.child("category").getValue();

                    //add to arraylists
                    categoryIdArrayList.add(categoryId);
                    categoryTitleArrayList.add(categoryTitle);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private String title= "", description="", time="", price="", phone="";
    private void validateData() {

        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();
        time = binding.timeEt.getText().toString().trim();
        price = binding.priceEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Внесете наслов на огласот!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Внесете опис на огласот!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(time)){
            Toast.makeText(this, "Внесете времетраење на часот!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Внесете цена на час!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Внесете телефон за контакт", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(selectedCategoryTitle)){
            Toast.makeText(this, "Изберете категорија!", Toast.LENGTH_SHORT).show();
        }
        else {
            //if is everything ok -validation passed
            //upload Ad to database
            uploadAd();
        }
    }

    private void uploadAd() {
        //to database
        progressDialog.setMessage("Додавање на оглас во база..");
        progressDialog.show();

        //current user id
        String uid =firebaseAuth.getUid();
        //timestamp
        long timestamp = System.currentTimeMillis();


        Ad ad = new Ad(uid,""+timestamp,title,description,""+time,""+price,""+phone,selectedCategoryId,timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.child(""+timestamp)
                .setValue(ad)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

        //cleaning form
        binding.titleEt.setText("");
        binding.descriptionEt.setText("");
        binding.timeEt.setText("");
        binding.priceEt.setText("");
        binding.phoneEt.setText("");
        binding.categoryTv.setText("");
    }

    private String selectedCategoryId, selectedCategoryTitle;
    private void categoryPickDialog() {
        //get data (categories) form firebase

        String[] categoriesArray = new String[categoryTitleArrayList.size()];
        for(int i =0;i<categoryTitleArrayList.size();i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }

        //alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Одбери категорија")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //handle item click, get clicked item from dialog list
                        selectedCategoryTitle = categoryTitleArrayList.get(i);
                        selectedCategoryId = categoryIdArrayList.get(i);

                        //set to categoryTv
                        binding.categoryTv.setText(selectedCategoryTitle);
                    }
                })
                .show();

    }
}