package com.proba_mpp.zakazi_cas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proba_mpp.zakazi_cas.Filter.FilterCategoryUser;
import com.proba_mpp.zakazi_cas.ListingActivity;
import com.proba_mpp.zakazi_cas.Model.Category;
import com.proba_mpp.zakazi_cas.databinding.RowCategoryUserBinding;

import java.util.ArrayList;

public class AdapterCategoryUser extends RecyclerView.Adapter<AdapterCategoryUser.CategoryHolderUser> implements Filterable {

    //binding
    private RowCategoryUserBinding binding;

    //context
    private Context context;

    //array list of categories
    public ArrayList<Category> categoryArrayList, categoryFilterArrayList;

    //filter
    private FilterCategoryUser filterCategoryUser;

    public AdapterCategoryUser(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.categoryFilterArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoryHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryUserBinding.inflate(LayoutInflater.from(context), parent, false);

        return new CategoryHolderUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolderUser holder, int position) {
        //get data

        Category category = categoryArrayList.get(position);
        String category_name = category.getCategory();

        //set data to holder
        holder.categoryTv.setText(category_name);

        //click item -> category clicked -> all ads
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListingActivity.class);
                intent.putExtra("categoryId", category_name);
                intent.putExtra("categoryTitle", category_name);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filterCategoryUser == null){
            filterCategoryUser = new FilterCategoryUser(categoryFilterArrayList, AdapterCategoryUser.this);
        }
        return filterCategoryUser;
    }

    //class Holder
    public class CategoryHolderUser extends RecyclerView.ViewHolder {

        private TextView categoryTv;

        public CategoryHolderUser(@NonNull View itemView) {
            super(itemView);

            categoryTv = binding.categoryTv;
        }
    }
}
