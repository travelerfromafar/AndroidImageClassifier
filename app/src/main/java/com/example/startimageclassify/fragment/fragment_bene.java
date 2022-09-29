package com.example.startimageclassify.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.startimageclassify.FavoritesActivity;
import com.example.startimageclassify.R;
import com.example.startimageclassify.camera;
import com.example.startimageclassify.classes.ItemClick;
import com.example.startimageclassify.classes.vegetable;
import com.example.startimageclassify.myAdapterBeneFoes;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class fragment_bene extends Fragment implements ItemClick {

    View view;
//    private SharedViewModel viewModel;

    RecyclerView recyclerViewBene;
    ArrayList<vegetable> vegetableArrayListBene;
    fragment_main ListBene;
    myAdapterBeneFoes myAdapterBenefits;
    String subBene = "Benefits", getPath, DocuPath, value;
    String [] vegName;

    FirebaseFirestore db;
    ProgressDialog progressDialog;

    public static final String TAG = "BeneLogs";

    @Override
    public void onItemClick(int position) {
        Checker(position);
    }

    public interface UsersProvider {
//        ArrayList<vegetable> getVegsBene();
        String getVegsName();
    }

    private UsersProvider usersProvider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        usersProvider = (UsersProvider) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bene, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        vegName = getResources().getStringArray(R.array.VegRecog);
        value = usersProvider.getVegsName();

        RecycleInit();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        ArrayList<String> values = getArguments().getStringArrayList("key");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void RecycleInit(){

        recyclerViewBene = view.findViewById(R.id.FragViewBene);
        recyclerViewBene.setHasFixedSize(true);
        recyclerViewBene.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DocuPath = value;

        vegetableArrayListBene = new ArrayList<vegetable>();

        myAdapterBenefits = new myAdapterBeneFoes(this.getContext(), vegetableArrayListBene,this);

        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        if (vegetableArrayListBene != null){
            ListenerBeneFoes(subBene);
        } else {
            vegetableArrayListBene.clear();
            ListenerBeneFoes(subBene);
        }

        recyclerViewBene.setAdapter(myAdapterBenefits);
    }

    private void ListenerBeneFoes(String sub) {

        db = FirebaseFirestore.getInstance();
        db.collection("vegetables_of").document(DocuPath).collection(sub)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            EndProgressDia();
                            Log.e("Firestore now working", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                vegetableArrayListBene.add(dc.getDocument().toObject(vegetable.class));
                            }
                            myAdapterBenefits.notifyDataSetChanged();
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                        if (vegetableArrayListBene.isEmpty()){
                            EndProgressDia();
                            Toast.makeText(getContext(), "No Data is available", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void OpenTab(String NamePos) {
        Intent i = new Intent(getActivity(), fragment_main.class);
//        i.putExtra("key", vegetableArrayListBene.get(NamePos).getName());
        i.putExtra("key", NamePos);
        startActivity(i);
    }

    private void EndProgressDia(){
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    public void Checker(int GetPos) {
        String getV = vegetableArrayListBene.get(GetPos).getName();
        if (getV.equalsIgnoreCase("carrot") ||
                getV.equalsIgnoreCase("spinach") ||
                getV.equalsIgnoreCase(vegName[0]) ||
                getV.equalsIgnoreCase(vegName[1]) ||
                getV.equalsIgnoreCase(vegName[2]) ||
                getV.equalsIgnoreCase(vegName[3]) ||
                getV.equalsIgnoreCase(vegName[4]) ||
                getV.equalsIgnoreCase(vegName[5]) ||
                getV.equalsIgnoreCase(vegName[6]) ||
                getV.equalsIgnoreCase(vegName[7]) ||
                getV.equalsIgnoreCase(vegName[8]) ||
                getV.equalsIgnoreCase(vegName[9])) {
            OpenTab(getV);}
        else {
            Toast.makeText(getActivity(), "Not Available", Toast.LENGTH_SHORT).show();
        }
    }
}