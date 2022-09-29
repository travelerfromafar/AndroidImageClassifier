package com.example.startimageclassify.vegetable;

import androidx.appcompat.app.AppCompatActivity;
import com.example.startimageclassify.R;
import com.example.startimageclassify.classes.vegetable;
import com.example.startimageclassify.myAdapterBeneFoes;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ShowVegetable extends AppCompatActivity {

    RecyclerView recyclerViewBene, recyclerViewFoes;
    ArrayList<vegetable> vegetableArrayListBene, vegetableArrayListFoes;
    myAdapterBeneFoes myAdapterBenefits, myAdapterFoes;
    String subBene, subFoes, getPath, DocuPath, value;

    FirebaseFirestore db;
    ProgressDialog progressDialog;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vegetable);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        recyclerViewBene = findViewById(R.id.RecyclerViewBene);
        recyclerViewBene.setHasFixedSize(true);
        recyclerViewBene.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewFoes = findViewById(R.id.RecyclerViewFoes);
        recyclerViewFoes.setHasFixedSize(true);
        recyclerViewFoes.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
//        vegetableArrayListBene = new ArrayList<vegetable>();
//        myAdapterBenefits = new myAdapterBeneFoes(ShowVegetable.this, vegetableArrayListBene);
//        vegetableArrayListFoes = new ArrayList<vegetable>();
//        myAdapterFoes = new myAdapterBeneFoes(ShowVegetable.this, vegetableArrayListFoes);

        recyclerViewBene.setAdapter(myAdapterBenefits);
        recyclerViewFoes.setAdapter(myAdapterFoes);

        subBene = "Benefits";
        subFoes = "Foes";
        textView = findViewById(R.id.VegetableName);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("key");

            //The key argument here must match that used in the other activity
            getPath = value;
            DocumentGet();
        }
        textView.setText(value.toUpperCase());
    }

    private void DocumentGet(){
        DocuPath = getPath.substring(0, 1).toUpperCase() + getPath.substring(1).toLowerCase();

        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        vegetableArrayListBene.clear();
        ListenerBeneFoes(subBene);

        vegetableArrayListFoes.clear();
        ListenerBeneFoes(subFoes);
    }

    private void ListenerBeneFoes(String sub) {
        String subRes = sub;
        db.collection("Vegetables").document(DocuPath).collection(sub)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore now working", error.getMessage());
                            return;
                        } else if (subRes.matches("Benefits")) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    vegetableArrayListBene.add(dc.getDocument().toObject(vegetable.class));
                                }
                                myAdapterBenefits.notifyDataSetChanged();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        } else if (subRes.matches("Foes")) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    vegetableArrayListFoes.add(dc.getDocument().toObject(vegetable.class));
                                }
                                myAdapterFoes.notifyDataSetChanged();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}