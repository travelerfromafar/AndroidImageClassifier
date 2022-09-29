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

public class ListRecycler extends RecyclerView.Adapter<ListRecycler.MyViewHolder> implements Filterable {

    Context context;
    public ArrayList<listvegs> listVegs;
    ArrayList<listvegs> listVegsNew;

    private final ItemClick vegClick;
    boolean isAtFav = false;

    public ListRecycler(Context context, ArrayList<listvegs> listVegs, ItemClick vegClick) {
        this.context = context;
        this.listVegs = listVegs;
        listVegsNew = new ArrayList<>(listVegs);
        this.vegClick = vegClick;
    }

    @NonNull
    @Override
    public ListRecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false);

        return new MyViewHolder(v, vegClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecycler.MyViewHolder holder, int position) {
        listvegs listVegsPos = listVegs.get(position);
//        CheckIfinFB(listVegsPos.vegName, holder);

        holder.name.setText(listVegsPos.getName());
        holder.class_.setText(listVegsPos.getFilclass());
        Glide.with(this.context).load(listVegsPos.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listVegs.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, class_;
        ImageView image;
        ImageButton btnAddFavRem;
//        DatabaseReference dataref;

        public MyViewHolder(@NonNull View itemView, ItemClick itemClick) {
            super(itemView);
            name = itemView.findViewById(R.id.VegText);
            class_ = itemView.findViewById(R.id.TextViewClass);
            image = itemView.findViewById(R.id.ImageVegList);
            btnAddFavRem = itemView.findViewById(R.id.btnFavRemove);
            btnAddFavRem.setEnabled(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    if (itemClick != null) {

                        if (pos != RecyclerView.NO_POSITION){
                            itemClick.onItemClick(pos);
                        }
                    }
                }
            });
        }
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
}
