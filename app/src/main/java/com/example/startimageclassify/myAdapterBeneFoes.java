package com.example.startimageclassify;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.startimageclassify.classes.ItemClick;
import com.example.startimageclassify.classes.vegetable;

import java.util.ArrayList;

public class myAdapterBeneFoes extends RecyclerView.Adapter<myAdapterBeneFoes.MyViewHolder> {

    Context context;
    ArrayList<vegetable> vegetableArrayList;

    private final ItemClick itemClick;

    public myAdapterBeneFoes(Context context, ArrayList<vegetable> vegetableArrayList, ItemClick ItemClick) {
        this.context = context;
        this.vegetableArrayList = vegetableArrayList;
        this.itemClick = ItemClick;
    }

    @NonNull
    @Override
    public myAdapterBeneFoes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new MyViewHolder(v, itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapterBeneFoes.MyViewHolder holder, int position) {

        vegetable vegetable = vegetableArrayList.get(position);

        holder.name.setText(vegetable.getName());
        holder.description.setText(vegetable.getDescription());
        Glide.with(this.context).load(vegetable.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return vegetableArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, description;
        ImageView image;

        public MyViewHolder(@NonNull View itemView, ItemClick itemClick) {
            super(itemView);
            name = itemView.findViewById(R.id.tvVegetableName);
            description = itemView.findViewById(R.id.tvBenefits);
            image = itemView.findViewById(R.id.VegeImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClick != null) {
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            itemClick.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}