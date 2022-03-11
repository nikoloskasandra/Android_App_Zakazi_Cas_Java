package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.databinding.ActivityAdEditBinding;
import com.proba_mpp.zakazi_cas.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
    //binding
    private ActivityDetailsBinding binding;

    //Ad id get from intent
    private String adId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Ad id get form intent
        Intent intent = getIntent();
        adId = intent.getStringExtra("adId");


        loadAdDetails();

        //back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //go to public profile

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
                ref.child(adId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //get data

                                String userId = ""+snapshot.child("uid").getValue();

                                //intent with user id
                                Intent i = new Intent(DetailsActivity.this, PublicProfileActivity.class);
                                i.putExtra("uid", userId);
                                startActivity(i);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        //call btn
        binding.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_number();
            }
        });

    }

    private void phone_number() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.child(adId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data

                        String phone = ""+snapshot.child("phone").getValue();

                        //MAKE A CALL -- ADD CODE


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadAdDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.child(adId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data
                        String title = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String phone = ""+snapshot.child("phone").getValue();
                        String price = ""+snapshot.child("price").getValue();
                        String time = ""+snapshot.child("time").getValue();
                        String userId = ""+snapshot.child("uid").getValue();

                        //set data
                        binding.titleTv.setText(title);
                        binding.descriptionTv.setText(description);
                        binding.phoneTv.setText(phone);
                        binding.priceTv.setText(price);
                        binding.timeTv.setText(time);

                        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Users");
                        refUser.child(userId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //get - set
                                        String name = ""+snapshot.child("full_name").getValue();

                                        binding.nameTv.setText(name);
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
}