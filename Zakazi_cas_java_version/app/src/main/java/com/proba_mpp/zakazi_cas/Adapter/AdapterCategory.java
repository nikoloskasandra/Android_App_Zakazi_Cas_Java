package com.proba_mpp.zakazi_cas.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proba_mpp.zakazi_cas.Filter.FilterCategory;
import com.proba_mpp.zakazi_cas.Model.Category;
import com.proba_mpp.zakazi_cas.databinding.RowCategoryBinding;

import java.util.ArrayList;

public class AdapterCategory  extends RecyclerView.Adapter<AdapterCategory.HolderCategory> implements Filterable {

    //context
    private Context context;
    //list of categories
    public ArrayList<Category> categoryArrayList, filterList;
    //binding
    private RowCategoryBinding binding;

    //filter
    private FilterCategory filterCategory;

    public AdapterCategory(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.filterList = categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //bind row category
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        //get data
        Category category = categoryArrayList.get(position);
        String category_name = category.getCategory();

        //set data to holder
        holder.categoryTv.setText(category_name);

        //delete category button
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirmm delete dialog
                AlertDialog.Builder  builder = new AlertDialog.Builder(context);
                builder.setTitle("Избриши")
                        .setMessage("Дали сте сигурни дека сакате да ја избришете категоријата?")
                        .setPositiveButton("Избриши", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete
                                Toast.makeText(context, "Бришење на категорија..", Toast.LENGTH_SHORT).show();
                                deleteCategory(category, holder);

                            }

                        }).setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .show();
            }
        });
    }

            private void deleteCategory(Category category, HolderCategory holder) {

                //which category and which holder should be deleted

                //get id of category to delete
                String id = category.getCategory();
                DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Categories");
                ref.child(id)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //delete successfully
                                Toast.makeText(context, "Успешно избришана категорија!",  Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //failed to delete
                                Toast.makeText(context, "Настана грешка при бришење на категоријата!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterCategory == null){

            filterCategory = new FilterCategory(filterList, this);

        }

        return filterCategory;
    }

    //view holder class to hold UI views for row_category
    public class HolderCategory extends RecyclerView.ViewHolder {

        //ui views
        private TextView categoryTv;
        private ImageButton deleteBtn;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            //init views
            categoryTv = binding.categoryTv;
            deleteBtn = binding.deleteBtn;

        }
    }
}
