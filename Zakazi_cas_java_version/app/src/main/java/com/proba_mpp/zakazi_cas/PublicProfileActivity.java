package com.proba_mpp.zakazi_cas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.proba_mpp.zakazi_cas.databinding.ActivityPublicProfileBinding;

public class PublicProfileActivity extends AppCompatActivity {

    private ActivityPublicProfileBinding binding;

    //get userid from intent - DetailsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
    }
}