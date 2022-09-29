package com.example.startimageclassify.classes;

import android.content.Context;
import android.icu.text.IDNA;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startimageclassify.R;
import com.example.startimageclassify.myAdapterBeneFoes;

import java.util.ArrayList;
import java.util.List;

public class InfoTableAdapter extends RecyclerView.Adapter<InfoTableAdapter.MyViewHolder> {

    Context context;
    ArrayList<Spacing> spacingArrayList;

    public InfoTableAdapter(Context context, ArrayList<Spacing> spacingArrayList) {
        this.context = context;
        this.spacingArrayList = spacingArrayList;
    }

    @NonNull
    @Override
    public InfoTableAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_info_layout,parent, false);

        return new InfoTableAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoTableAdapter.MyViewHolder holder, int position) {

//        Spacing spacing = spacingArrayList.get(position);

        holder.vege_NameInfo.setText(spacingArrayList.get(position).getVegeName());
        holder.vege_SpacingInfo.setText(spacingArrayList.get(position).getVegeSpacing());

    }

    @Override
    public int getItemCount() {
        return spacingArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView vege_NameInfo, vege_SpacingInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            vege_NameInfo = itemView.findViewById(R.id.LabelPlant_item);
            vege_SpacingInfo = itemView.findViewById(R.id.LabelSpacing_item);
        }
    }
}
