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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.Model.Ad;
import com.proba_mpp.zakazi_cas.databinding.ActivityAdEditBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class AdEditActivity extends AppCompatActivity {

    private ActivityAdEditBinding binding;

    //ad id get from intent started from AdapterListingAdUserProfile
    private String adId;

    //progress dialog
    private ProgressDialog progressDialog;

    //array list of category titles
    private ArrayList<String> categoryTitlesArrayList, categoryIdArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding =ActivityAdEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ad id get from intent started from AdapterListingAdUserProfile
        adId = getIntent().getStringExtra("adId");

        //setup
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Ве молиме почекајте");
        progressDialog.setCanceledOnTouchOutside(false);

        loadCategories();
        loadAdInfo();

        //picked category
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }
        });

        //upload clicked
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();

            }
        });

        //back button
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private String title ="", description="", price ="", phone="", time="";
    private void validateData() {
        //Edited Ad info validation

        //get data
        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();
        price = binding.priceEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        time = binding.timeEt.getText().toString().trim();

        //validate
        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Внесете наслов на огласот..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Внесете опис на огласот..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Внесете цена на часот..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Внесете телефонски број...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(time)){
            Toast.makeText(this, "Внесете времетраење..",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(selectedCategoryId)){
            Toast.makeText(this, "Изберете категорија ..",Toast.LENGTH_SHORT).show();
        }else {
            //data is valid -> update data
            update();

        }

    }

    private void update() {
        //show progress dialog
        progressDialog.setMessage("Измена во тек..");
        progressDialog.show();

        //update data -> database (hashmap used) --> can be done another way
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("price",""+price);
        hashMap.put("phone",""+phone);
        hashMap.put("time",""+time);
        hashMap.put("categoryId", ""+selectedCategoryId);

        //updating
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.child(adId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(AdEditActivity.this, "Измената на огласот е успешна..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdEditActivity.this,"Измената на огласот не е успешна..",Toast.LENGTH_SHORT).show();
                    }
                });

        //redirect to user profile
        startActivity(new Intent(AdEditActivity.this, ProfileActivity.class ));
    }

    private void loadAdInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.child(adId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data - Ad
                        selectedCategoryId = ""+snapshot.child("categoryId").getValue();
                        String title = ""+snapshot.child("title").getValue();
                        String description =""+snapshot.child("description").getValue();
                        String phone = ""+snapshot.child("phone").getValue();
                        String price =""+snapshot.child("price").getValue();
                        String time = ""+snapshot.child("time").getValue();

                        //set data - Ad
                        binding.titleEt.setText(title);
                        binding.descriptionEt.setText(description);
                        binding.phoneEt.setText(phone);
                        binding.priceEt.setText(price);
                        binding.timeEt.setText(time);

                        //Categories - category info
                        DatabaseReference refAdCategory = FirebaseDatabase.getInstance().getReference("Categories");
                        refAdCategory.child(selectedCategoryId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //get category
                                        String category = ""+snapshot.child("category").getValue();

                                        //set category
                                        binding.categoryTv.setText(category);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String selectedCategoryId="", selectedCategoryTitle="";

    private void categoryDialog(){

        //make string array from arraylist of string
        String[] categoriesArray = new String[categoryTitlesArrayList.size()];
        for(int i =0;i< categoryTitlesArrayList.size();i++){
            categoriesArray[i] = categoryTitlesArrayList.get(i);
        }

        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Изберете категорија")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedCategoryId = categoryIdArrayList.get(i);
                        selectedCategoryTitle = categoryTitlesArrayList.get(i);


                        //set to textview
                        binding.categoryTv.setText(selectedCategoryTitle);
                    }
                })
                .show();

    }
    private void loadCategories() {

        categoryTitlesArrayList = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIdArrayList.clear();
                categoryTitlesArrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){

                    String id = ""+ds.child("category").getValue();
                    String category = ""+ds.child("category").getValue();

                    categoryIdArrayList.add(id);
                    categoryTitlesArrayList.add(category);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}