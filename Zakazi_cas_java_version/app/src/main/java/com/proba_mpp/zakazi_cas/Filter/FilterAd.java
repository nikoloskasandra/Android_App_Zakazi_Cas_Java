package com.proba_mpp.zakazi_cas.Filter;

import android.widget.Filter;

import com.proba_mpp.zakazi_cas.Adapter.AdapterListingAd;
import com.proba_mpp.zakazi_cas.Model.Ad;

import java.util.ArrayList;

//FILTERING BY AD NAME
public class FilterAd  extends Filter {

    //array list for searching
    ArrayList<Ad> filteringList;

    //adapter in which filter will be used
    AdapterListingAd adapterListingAd;

    public FilterAd(ArrayList<Ad> filteringList, AdapterListingAd adapterListingAd) {
        this.filteringList = filteringList;
        this.adapterListingAd = adapterListingAd;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();

        if(charSequence != null && charSequence.length()>0){

            //-- avoid low caps
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Ad> filteredAds = new ArrayList<>();

            for(int i=0; i<filteringList.size(); i++){
                if(filteringList.get(i).getTitle().toUpperCase().contains(charSequence)){
                    filteredAds.add(filteringList.get(i));
                }
            }
            filterResults.count = filteredAds.size();
            filterResults.values = filteredAds;
        }
        else {
            filterResults.count = filteringList.size();
            filterResults.values = filteringList;
        }

        return filterResults;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        //apply filter changes

        adapterListingAd.adArrayList = (ArrayList<Ad>)filterResults.values;
        adapterListingAd.notifyDataSetChanged();
    }
}
