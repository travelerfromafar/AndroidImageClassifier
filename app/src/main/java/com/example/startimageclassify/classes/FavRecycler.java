package com.example.startimageclassify.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startimageclassify.R;
import com.example.startimageclassify.fragment.fragment_main;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavRecycler extends RecyclerView.Adapter<FavRecycler.MyViewHolder> {

    Context context;
    public ArrayList<favrecyclerobj> favlistVegs;
    private final ItemClick vegClick;
    private final ItemRemove remClick;
    ReturnVal returnVal;
    FirebaseAuth CurAuth;

    public FavRecycler(Context context, ArrayList<favrecyclerobj> listVegs, ItemClick vegClick, ItemRemove remClick) {
        this.context = context;
        this.favlistVegs = listVegs;
        this.vegClick = vegClick;
        this.remClick = remClick;
    }

    @NonNull
    @Override
    public FavRecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false);

        return new FavRecycler.MyViewHolder(v, vegClick, remClick);
    }

    @Override
    public void onBindViewHolder(@NonNull FavRecycler.MyViewHolder holder, int position) {
        favrecyclerobj listVegsPos = favlistVegs.get(position);
//        CheckIfinFB(listVegsPos.vegName, holder);

        holder.name.setText(listVegsPos.getName());
        holder.class_.setText(listVegsPos.getFilclass());
        String name = listVegsPos.getName();
//        returnVal = new ReturnVal(name, holder);
        ImageSetUp(name, holder);
//        Glide.with(this.context).load(listVegsPos.getImage()).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return favlistVegs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, class_;
        ImageView image;
        ImageButton btnAddFavRem;

        public MyViewHolder(@NonNull View itemView, ItemClick itemClick, ItemRemove itemRemove) {
            super(itemView);
            name = itemView.findViewById(R.id.VegText);
            class_ = itemView.findViewById(R.id.TextViewClass);
            image = itemView.findViewById(R.id.ImageVegList);
            btnAddFavRem = itemView.findViewById(R.id.btnFavRemove);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    if (itemClick != null) {

                        if (pos != RecyclerView.NO_POSITION) {
                            itemClick.onItemClick(pos);
                        }
                    }
                }
            });

            btnAddFavRem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    if (itemRemove != null) {

                        if (pos != RecyclerView.NO_POSITION) {
                            itemRemove.onItemRemove(pos);
                        }
                    }
                }
            });
        }
    }

    private void ImageSetUp (String name, FavRecycler.MyViewHolder holder){
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
