package com.proba_mpp.zakazi_cas.Filter;

import android.widget.Filter;

import com.proba_mpp.zakazi_cas.Adapter.AdapterCategoryUser;
import com.proba_mpp.zakazi_cas.Model.Category;

import java.util.ArrayList;

public class FilterCategoryUser extends Filter {

    //array list for searching
    private ArrayList<Category> filteringList;

    //adpter in which filter will be used
    private AdapterCategoryUser adapterCategoryUser;

    public FilterCategoryUser(ArrayList<Category> filteringList, AdapterCategoryUser adapterCategoryUser) {
        this.filteringList = filteringList;
        this.adapterCategoryUser = adapterCategoryUser;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();

        if(charSequence != null && charSequence.length()>0){

            //change - avoid low caps letters -- all uppercase
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Category> filteredCategories = new ArrayList<>();

            for(int i=0;i<filteringList.size();i++){
                if(filteringList.get(i).getCategory().toUpperCase().contains(charSequence)){
                    filteredCategories.add(filteringList.get(i));
                }
            }
            filterResults.count = filteredCategories.size();
            filterResults.values = filteredCategories;
        }else {
            filterResults.count = filteringList.size();
            filterResults.values = filteringList;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        //apply filter changes
        adapterCategoryUser.categoryArrayList = (ArrayList<Category>)filterResults.values;
        adapterCategoryUser.notifyDataSetChanged();
    }
}
