package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.Adapter.AdapterListingAdUserProfile;
import com.proba_mpp.zakazi_cas.Model.Ad;
import com.proba_mpp.zakazi_cas.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    //binding
    private ActivityProfileBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //array list of Ads created from user
    private ArrayList<Ad> adArrayList;

    //adapter
    private AdapterListingAdUserProfile adapterListingAdUserProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        firebaseAuth  = FirebaseAuth.getInstance();

        checkUser();

        loadAdsCreatedByUser();

        //logout button
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //Create Ad Button
        binding.addAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfLoggedIn();
            }
        });

        //Pocetna
        binding.listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, DashboardUserActivity.class));
            }
        });
    }

    private void loadAdsCreatedByUser() {
        adArrayList = new ArrayList<>();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.orderByChild("uid").equalTo(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adArrayList.clear();

                        for(DataSnapshot ds: snapshot.getChildren()){
                            //Ad
                            Ad ad = ds.getValue(Ad.class);
                            //add Ad to list
                            adArrayList.add(ad);
                        }
                        adapterListingAdUserProfile = new AdapterListingAdUserProfile(ProfileActivity.this, adArrayList);
                        binding.adUserRv.setAdapter(adapterListingAdUserProfile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkIfLoggedIn() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(ProfileActivity.this, AdActivity.class));
        }
        else {
            //never accessed
        }
    }

    private void checkUser() {

        //get Current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }else {
            //logged in user
            String email= firebaseUser.getEmail();
            binding.subTitleTv.setText(email);
        }
    }
}