package com.proba_mpp.zakazi_cas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.Adapter.AdapterCategoryUser;
import com.proba_mpp.zakazi_cas.Model.Category;
import com.proba_mpp.zakazi_cas.databinding.ActivityDashboardUserBinding;

import java.util.ArrayList;

public class DashboardUserActivity extends AppCompatActivity {

    //binding
    private ActivityDashboardUserBinding binding;

    //firebaseauth
    private FirebaseAuth firebaseAuth;

    //list of categories
    private ArrayList<Category> categoryArrayList;

    //adapter - categoryadapteruser
    private AdapterCategoryUser adapterCategoryUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        loadCategories();

        //back button
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Create Ad button
        binding.addAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if user is logged in !
                checkUser();

            }
        });



        //search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //called when user is typing
                try {
                    adapterCategoryUser.getFilter().filter(charSequence);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //should do:


            //4. adding Ad - OGLAS
            //5. showing all ads - oglasi

    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(DashboardUserActivity.this, AdActivity.class));
        }else {
            //if the user is not logged in --redirect to LoginActivity
            Toast.makeText(this, "Ве молиме најавете се!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardUserActivity.this, LoginActivity.class));

        }
    }

    private void loadCategories() {
        categoryArrayList = new ArrayList<>();

        //get all categories from database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear array list of categories
                categoryArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    Category category = ds.getValue(Category.class);
                    categoryArrayList.add(category);
                }
                //set up adapter
                adapterCategoryUser = new AdapterCategoryUser(DashboardUserActivity.this, categoryArrayList);
                //set adapter to recyclerview
                binding.categoriesRv.setAdapter(adapterCategoryUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}