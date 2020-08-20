package com.example.covid_19tracker.ui.home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.StateWise;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ImageViewHolder> implements Filterable {
    Context context;
    List<StateWise> stateWises;
    List<StateWise> copyList;
    public StateAdapter(Context context, List<StateWise> stateWises){
        this.context = context;
        this.stateWises = stateWises;
        copyList = new ArrayList<>(stateWises);
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        if(position == 0) {
            holder.state.setText("STATE");
            holder.active.setText("ACTIVE");
            holder.conf.setText("CONFIRMED");
            holder.rec.setText("RECOVERED");
            holder.dead.setText("DEAD");
            holder.state.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.active.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.conf.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.rec.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.dead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            StateWise curr = stateWises.get(position);
            holder.state.setText(curr.getState());
            holder.active.setText(curr.getActive());
            holder.conf.setText(curr.getConfirmed());
            holder.rec.setText(curr.getRecovered());
            holder.dead.setText(curr.getDeaths());
        }
    }

    @Override
    public int getItemCount() {
        return stateWises.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView state,conf,active,dead,rec;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.state);
            conf = itemView.findViewById(R.id.confirmed);
            active = itemView.findViewById(R.id.active);
            dead = itemView.findViewById(R.id.dead);
            rec = itemView.findViewById(R.id.recovered);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StateWise> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(copyList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(StateWise s:copyList){
                    if(s.getState().toLowerCase().trim().startsWith(filterPattern) || s.getState().toLowerCase().trim().equals(filterPattern)){
                        filteredList.add(s);                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stateWises.clear();
            stateWises.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
