package com.proba_mpp.zakazi_cas.Filter;

import android.widget.Filter;

import com.proba_mpp.zakazi_cas.Adapter.AdapterCategory;
import com.proba_mpp.zakazi_cas.Model.Category;

import java.util.ArrayList;

public class FilterCategory extends Filter {

    //array list for searching
    private ArrayList<Category> filteringList;

    //adapter in which filter would be implemented
    private AdapterCategory adapterCategory;

    public FilterCategory(ArrayList<Category> filteringList, AdapterCategory adapterCategory) {
        this.filteringList = filteringList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        //values should not be null

        if(charSequence != null && charSequence.length()>0){

            //change .. avoid case sens
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Category> filteredCategories = new ArrayList<>();

            for(int i=0; i< filteringList.size();i++ ){
                //validate
                if(filteringList.get(i).getCategory().toUpperCase().contains(charSequence)){
                    //add to filteredlist
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
        adapterCategory.categoryArrayList = (ArrayList<Category>)filterResults.values;

        //notify
        adapterCategory.notifyDataSetChanged();
    }
}
