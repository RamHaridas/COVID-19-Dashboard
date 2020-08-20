package com.example.covid_19tracker.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.Districts;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.STATES;
import com.example.covid_19tracker.ui.home.StateAdapter;
import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ImageHolder> implements Filterable {

    Context context;
    List<STATES> statesList;
    List<Districts> districtslist;
    private List<Districts> copyList;
    public DistrictAdapter(Context context, List<STATES> statesList, List<Districts> districtslist){
        this.context = context;
        this.statesList = statesList;
        this.districtslist = districtslist;
        copyList = new ArrayList<>(districtslist);
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.district_item,parent,false);

        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        if(position == 0){
            holder.dist.setText("DISTRICTS");
            holder.conf.setText("CONFIRM");
        }else {
            if(districtslist.get(position).getZone().equals("RED")){
                holder.dist.setTextColor(Color.RED);
                holder.conf.setTextColor(Color.RED);
            }else if(districtslist.get(position).getZone().equals("GREEN")){
                holder.dist.setTextColor(Color.GREEN);
                holder.conf.setTextColor(Color.GREEN);
            }else if(districtslist.get(position).getZone().equals("ORANGE")){
                holder.dist.setTextColor(Color.rgb(  240, 119, 19  ));
                holder.conf.setTextColor(Color.rgb(  255, 131, 0  ));
            }

            holder.dist.setText(districtslist.get(position).getName());
            holder.conf.setText(String.valueOf(districtslist.get(position).getConfirm()));
        }
    }

    @Override
    public int getItemCount() {
        return districtslist.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder{
        TextView dist,conf;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            dist = itemView.findViewById(R.id.district);
            conf = itemView.findViewById(R.id.confirm1);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Districts> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(copyList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Districts d: copyList){
                    if(d.getName().toLowerCase().trim().startsWith(filterPattern) || d.getName().toLowerCase().trim().equals(filterPattern)){
                        filteredList.add(d);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            districtslist.clear();
            districtslist.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
