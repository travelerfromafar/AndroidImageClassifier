package com.example.startimageclassify.classes;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.startimageclassify.R;

import java.util.ArrayList;

public class ListRecyclerVeg extends RecyclerView.Adapter<ListRecyclerVeg.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<listvegs> listVegs;
    ArrayList<listvegs> listVegsNew;
    ReturnVal returnVal;

    private final ItemClick itemClick;

    public ListRecyclerVeg(Context context, ArrayList<listvegs> listVegs, ItemClick ItemClick) {
        this.context = context;
        this.listVegs = listVegs;
        listVegsNew = new ArrayList<>(listVegs);
        this.itemClick = ItemClick;
    }

    @NonNull
    @Override
    public ListRecyclerVeg.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false);

        return new MyViewHolder(v, itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecyclerVeg.MyViewHolder holder, int position) {

        listvegs listVeg = listVegs.get(position);

        holder.name.setText(listVeg.getName());
        holder.class_.setText(listVeg.getFilclass());
        String name = listVeg.getName();
//        returnVal = new ReturnVal(name, holder);
        ImageSetUp(name, holder);
//        Glide.with(this.context).load(listVeg.getImage()).into(holder.image);

//        holder.btnAddFavRem.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return listVegs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, class_;
        ImageView image;
        ImageButton btnAddFavRem;

        public MyViewHolder(@NonNull View itemView, ItemClick itemClick) {
            super(itemView);
            name = itemView.findViewById(R.id.VegText);
            class_ = itemView.findViewById(R.id.TextViewClass);
            image = itemView.findViewById(R.id.ImageVegList);
            btnAddFavRem = itemView.findViewById(R.id.btnFavRemove);
            btnAddFavRem.setVisibility(View.GONE);
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<listvegs> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listVegsNew);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (listvegs item : listVegsNew) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listVegs.clear();
            listVegs.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    private void ImageSetUp (String name, MyViewHolder holder){
        switch (name) {
            case "Bitter":
                holder.name.setText("Better Gourd");
                holder.image.setImageResource(R.drawable.veg_img_bitter);
                break;
            case "Cabbage":
                holder.image.setImageResource(R.drawable.veg_img_cab);
                break;
            case "Chili":
                holder.image.setImageResource(R.drawable.veg_img_chili);
                break;
            case "Eggplant":
                holder.image.setImageResource(R.drawable.veg_img_eggp);
                break;
            case "Ginger":
                holder.image.setImageResource(R.drawable.veg_img_gin);
                break;
            case "Lettuce":
                holder.image.setImageResource(R.drawable.veg_img_lett);
                break;
            case "Onion":
                holder.image.setImageResource(R.drawable.veg_img_oni);
                break;
            case "Pechay":
                holder.image.setImageResource(R.drawable.veg_img_bokchoy);
                break;
            case "Sweet Potato":
                holder.image.setImageResource(R.drawable.veg_img_swepo);
                break;
            case "Tomato":
                holder.image.setImageResource(R.drawable.veg_img_toma);
                break;
            default: holder.image.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}