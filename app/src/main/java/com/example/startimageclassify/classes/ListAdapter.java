package com.example.startimageclassify.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.startimageclassify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends ArrayAdapter<listvegs> {

    boolean IsAtFav = false;
    FirebaseAuth CurAuth = null;
    ImageButton btnFavList;


    public ListAdapter (Context context, ArrayList<listvegs> vegsArrayList){
        super(context, R.layout.item_list_layout, vegsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        CurAuth = FirebaseAuth.getInstance();
        listvegs vegs = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_layout, parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.ImageVegList);
        TextView textView = convertView.findViewById(R.id.VegText);
        TextView textViewFil = convertView.findViewById(R.id.TextViewClass);
//        btnFavList = convertView.findViewById(R.id.btnListAddFavRemove);

//        imageView.setImageResource(vegs.image);
        textView.setText(vegs.name);
        textViewFil.setText(vegs.filclass);

        if (CurAuth == null){
            Toast.makeText(getContext(),"Not logged in", Toast.LENGTH_SHORT).show();
        } else {
            CheckIfinFB(vegs.name);
        }
            btnFavList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"position: " + position, Toast.LENGTH_SHORT).show();
                    if (IsAtFav){
                        RemoveToFavFB(vegs.name);
                    } else {
                        AddToFavFB(vegs.name);
                    }
                    notifyDataSetChanged();
                }
            });

        return convertView;
    }

    private void AddToFavFB(String CurVeg) {
        FirebaseAuth CurAuth = FirebaseAuth.getInstance();
        if (CurAuth.getCurrentUser() == null){
            Toast.makeText(getContext(),"You are not logged in", Toast.LENGTH_SHORT).show();
        }
        else {
            long timestamp = System.currentTimeMillis();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("VegName", ""+CurVeg);
            hashMap.put("timestamp", ""+timestamp);

            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Users");
            dataref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Added to Favorites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Failed to add due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void RemoveToFavFB(String CurVeg) {
        if (CurAuth.getCurrentUser() == null){
            Toast.makeText(getContext(),"You are not logged in", Toast.LENGTH_SHORT).show();
        }
        else {

        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Users");
        dataref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(),"Removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Failed to remove due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }

    }

    private void CheckIfinFB(String CurVeg){
        DatabaseReference chref = FirebaseDatabase.getInstance().getReference("Users");
        chref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        IsAtFav = snapshot.exists();
                        if (IsAtFav){
                            btnFavList.setImageResource(R.drawable.ic_remove_fav);
                            btnFavList.getBackground().setTint(getContext().getColor(R.color.button_color_negative));
                        } else {
                            btnFavList.setImageResource(R.drawable.ic_add_fav);
                            btnFavList.getBackground().setTint(getContext().getColor(R.color.button_color_background));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
