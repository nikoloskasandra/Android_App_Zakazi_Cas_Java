package com.proba_mpp.zakazi_cas.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proba_mpp.zakazi_cas.AdEditActivity;
import com.proba_mpp.zakazi_cas.Model.Ad;
import com.proba_mpp.zakazi_cas.databinding.RowAdBinding;
import com.proba_mpp.zakazi_cas.databinding.RowAdUserBinding;

import java.util.ArrayList;

public class AdapterListingAdUserProfile extends RecyclerView.Adapter<AdapterListingAdUserProfile.AdUserHolder>{

    //context
    private Context context;

    //binding
    private RowAdUserBinding binding;

    //array list of Ads
    public ArrayList<Ad> adArrayList, adArrayFilteringList;

    //private FirebaseAuth firebaseAuth;


    public AdapterListingAdUserProfile(Context context, ArrayList<Ad> adArrayList) {
        this.context = context;
        this.adArrayList = adArrayList;
        this.adArrayFilteringList = adArrayList;
    }


    @NonNull
    @Override
    public AdUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //bind layout using view binding
        binding = RowAdUserBinding.inflate(LayoutInflater.from(context), parent,false);

        return new AdapterListingAdUserProfile.AdUserHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdUserHolder holder, int position) {

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

        //delete Ad


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirm dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Избриши")
                        .setMessage("Дали сте сигурни дека сакате да го избришете огласот?")
                        .setPositiveButton("Избриши", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete
                                deleteAd(ad,holder);
                            }
                        }).setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .show();
            }
        });


        //edit Ad
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAd(ad, holder);
            }
        });
    }

    private void editAd(Ad ad, AdUserHolder holder) {

        String adId = ad.getId();
        String title = ad.getTitle();
        String description = ad.getDescription();
        String phone = ad.getPhone();
        String price = ad.getPrice();
        String userId = ad.getUid(); //user id
        String categoryId =ad.getCategoryId(); // category id


        Intent intent = new Intent(context, AdEditActivity.class);
        intent.putExtra("adId", adId);
//        intent.putExtra("title", title);
//        intent.putExtra("description", description);
//        intent.putExtra("phone", phone);
//        intent.putExtra("price", price);
//        intent.putExtra("userId", userId);
//        intent.putExtra("categoryId", categoryId);
            context.startActivity(intent);

    }

    private void deleteAd(Ad ad, AdUserHolder holder) {
        String adId = ad.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ads");
        ref.child(adId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //deleted successfully
                        Toast.makeText(context, "Успешно избришан оглас!",  Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed
                        Toast.makeText(context, "Не успешно избришан оглас!",  Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return adArrayList.size();
    }

    class AdUserHolder extends RecyclerView.ViewHolder {

        private TextView titleTv;
        private TextView descriptionTv;
        private TextView phoneTv;
        private TextView nameTv;
        private TextView priceTv;
        private ImageButton editBtn;
        private ImageButton deleteBtn;



        public AdUserHolder(@NonNull View itemView) {

            super(itemView);

            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            phoneTv = binding.phoneTv;
            nameTv = binding.nameTv;
            priceTv = binding.priceTv;
            editBtn = binding.editBtn;
            deleteBtn = binding.deleteBtn;
        }
    }
}
