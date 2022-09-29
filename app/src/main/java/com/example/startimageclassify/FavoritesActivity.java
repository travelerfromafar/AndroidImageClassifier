package com.example.startimageclassify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.startimageclassify.classes.FavRecycler;
import com.example.startimageclassify.classes.ItemClick;
import com.example.startimageclassify.classes.ItemRemove;
import com.example.startimageclassify.classes.favrecyclerobj;
import com.example.startimageclassify.classes.listvegs;
import com.example.startimageclassify.fragment.fragment_main;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritesActivity extends AppCompatActivity implements ItemClick, ItemRemove {

    RecyclerView recyclerViewFavList;
    FavRecycler favRecycler;
    FirebaseAuth CurAuth;
    DatabaseReference dataref;
    ArrayList<favrecyclerobj> listVegs = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        recyclerViewFavList = findViewById(R.id.RecyclerFav);
        CurAuth = FirebaseAuth.getInstance();
        dataref = FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(CurAuth.getUid())).child("Favorites");

        recyclerViewFavList.setHasFixedSize(true);
        recyclerViewFavList.setLayoutManager(new LinearLayoutManager(this));

        favRecycler = new FavRecycler(this, listVegs, this, this);
        recyclerViewFavList.setAdapter(favRecycler);

        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listVegs != null) {
                    listVegs.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    favrecyclerobj listitem = dataSnapshot.getValue(favrecyclerobj.class);
                    listVegs.add(listitem);
                }
                favRecycler.notifyDataSetChanged();
                if (listVegs.isEmpty()){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(FavoritesActivity.this, "Favorite is empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadFBFav(){

    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(FavoritesActivity.this, "Postion " + position, Toast.LENGTH_LONG).show();
        OpenTab(position);
    }

    private void RemoveToFavFB(String CurVeg) {
        CurAuth = FirebaseAuth.getInstance();

        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Users");
        dataref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(FavoritesActivity.this,"Removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FavoritesActivity.this,"Failed to remove due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//        }

    }

    @Override
    public void onItemRemove(int position) {
        RemoveToFavFB(listVegs.get(position).name);
    }

    private void OpenTab(int NameRes) {
        Intent i = new Intent(FavoritesActivity.this, fragment_main.class);
        i.putExtra("key",  listVegs.get(NameRes).getName());
        startActivity(i);
    }
}