package com.example.covid_19tracker.ui.Info;

import android.content.Context;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.ui.home.StateAdapter;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ImageViewHolder> {

    Context context;
    List<InfoDetails> list;

    public InfoAdapter(Context context, List<InfoDetails> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.help_item,parent,false);
        return new InfoAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if(position == 0){
        }else{
            holder.state.setText(list.get(position).getState());
            holder.help.setText(list.get(position).getNumber());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView state,help;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.state_name);
            help = itemView.findViewById(R.id.help_number);
        }
    }
}
