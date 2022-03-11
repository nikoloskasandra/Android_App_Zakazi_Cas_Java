package com.proba_mpp.zakazi_cas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.platforminfo.UserAgentPublisher;
import com.proba_mpp.zakazi_cas.DetailsActivity;
import com.proba_mpp.zakazi_cas.Filter.FilterAd;
import com.proba_mpp.zakazi_cas.Model.Ad;
import com.proba_mpp.zakazi_cas.Model.User;
import com.proba_mpp.zakazi_cas.databinding.RowAdBinding;

import java.util.ArrayList;

public class AdapterListingAd extends RecyclerView.Adapter<AdapterListingAd.AdHolder> implements Filterable {
    //Adapter for Ads --> За листање на огласи

    //context
    private Context context;

    //ad array list
    public ArrayList<Ad> adArrayList, filterAdArrayList;

    //firebase
    private FirebaseAuth firebaseAuth;

    //row --> view binding
    private RowAdBinding binding;

    //filter
    private FilterAd filter;

    //search


    public AdapterListingAd(Context context, ArrayList<Ad> adArrayList) {
        this.context = context;
        this.adArrayList = adArrayList;
        this.filterAdArrayList = adArrayList;

    }

    @NonNull
    @Override
    public AdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind layout using view binding
        binding = RowAdBinding.inflate(LayoutInflater.from(context), parent,false);

        return new AdHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdHolder holder, int position) {
        //get data, set data

        //get data
        Ad ad = adArrayList.get(position);
        String title = ad.getTitle();
        String description = ad.getDescription();
        String phone = ad.getPhone();
        String price = ad.getPrice();
        String userId = ad.getUid(); //user id
        String categoryId =ad.getCategoryId(); // category id

        long timestamp = ad.getTimestamp(); //same as id


        //get user name
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(""+userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get user name
                        String name = ""+snapshot.child("full_name").getValue();

                        //set user name
                        holder.nameTv.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        //set data
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.phoneTv.setText(phone);
        holder.priceTv.setText(price);


        //make holder -> clickable
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("adId", ad.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return adArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
             filter= new FilterAd(filterAdArrayList,AdapterListingAd.this);
        }
        return filter;
    }

    class AdHolder extends RecyclerView.ViewHolder{

        private TextView titleTv;
        private TextView descriptionTv;
        private TextView phoneTv;
        private TextView nameTv;
        private TextView priceTv;
        private ImageButton moreBtn;

        public AdHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            phoneTv = binding.phoneTv;
            nameTv = binding.nameTv;
            priceTv = binding.priceTv;
            moreBtn = binding.moreBtn;

        }
    }


}
