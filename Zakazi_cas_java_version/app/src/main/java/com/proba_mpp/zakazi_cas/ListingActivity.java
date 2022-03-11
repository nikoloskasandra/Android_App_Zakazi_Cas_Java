package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.Adapter.AdapterListingAd;
import com.proba_mpp.zakazi_cas.Model.Ad;
import com.proba_mpp.zakazi_cas.databinding.ActivityAdBinding;
import com.proba_mpp.zakazi_cas.databinding.ActivityListingBinding;

import java.util.ArrayList;

public class ListingActivity extends AppCompatActivity {

    //binding
   private ActivityListingBinding binding;

   //array list
    private ArrayList<Ad> adArrayList;

    //adapter
    private AdapterListingAd adapterListingAd;

    //catId , catTitle - for opening write list of ads
    private String categoryId, categoryTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityListingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data from intent
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");

        //load Ad list
        loadAdList();

        //set Ad category
        binding.subTitleTv.setText(categoryTitle);

        //search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //search as typing ...
                try{
                    adapterListingAd.getFilter().filter(charSequence);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void loadAdList() {

        //init array list
        adArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adArrayList.clear();

                        for(DataSnapshot ds: snapshot.getChildren()){
                            //Ad
                            Ad ad = ds.getValue(Ad.class);
                            //add to list
                            adArrayList.add(ad);
                        }
                        adapterListingAd = new AdapterListingAd(ListingActivity.this, adArrayList);
                        binding.adRv.setAdapter(adapterListingAd);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}