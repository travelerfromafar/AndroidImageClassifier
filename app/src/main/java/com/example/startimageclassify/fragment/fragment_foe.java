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

import com.example.startimageclassify.R;
import com.example.startimageclassify.classes.ItemClick;
import com.example.startimageclassify.classes.vegetable;
import com.example.startimageclassify.myAdapterBeneFoes;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class fragment_foe extends Fragment implements ItemClick {

    View view;

    RecyclerView recyclerViewFoes;
    ArrayList<vegetable> vegetableArrayListFoes, vegeListFoe;
    myAdapterBeneFoes myAdapterFoes;
    String subFoes = "Foes", getPath, DocuPath, value;
    String [] vegName;

    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public void onItemClick(int position) {
        Checker(position);
    }

    public interface UsersProvider {
//        ArrayList<vegetable> getVegsFoes();
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
        view = inflater.inflate(R.layout.fragment_foe, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        vegName = getResources().getStringArray(R.array.VegRecog);
        value = usersProvider.getVegsName();
        //        fragment_main getVal = (fragment_main) getActivity();
//        assert getVal != null;
//        getPath = getVal.value;
        RecycleInit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
//            getPath = item;
//        });

//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setCancelable(false);
//
//        recyclerViewBene = view.findViewById(R.id.RecyclerViewBene);
//        recyclerViewBene.setHasFixedSize(true);
//        recyclerViewBene.setLayoutManager(new LinearLayoutManager(getContext()));
//        vegetableArrayListBene = new ArrayList<vegetable>();
//        myAdapterBenefits = new myAdapterBeneFoes(getContext(), vegetableArrayListBene);
//
//        recyclerViewBene.setAdapter(myAdapterBenefits);
//
//        subBene = "Benefits";
//        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
//        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), item ->{
//            getPath = item;
//        });
//
//        DocuPath = getPath.substring(0, 1).toUpperCase() + getPath.substring(1).toLowerCase();

    }

    private void RecycleInit(){
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setCancelable(false);

        recyclerViewFoes = view.findViewById(R.id.FragViewFoes);
        recyclerViewFoes.setHasFixedSize(true);
        recyclerViewFoes.setLayoutManager(new LinearLayoutManager(getContext()));

        DocuPath = value;

        vegetableArrayListFoes = new ArrayList<vegetable>();
        myAdapterFoes = new myAdapterBeneFoes(this.getContext(), vegetableArrayListFoes, this);

        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        if (vegetableArrayListFoes != null){
            ListenerBeneFoes(subFoes);
        } else {
            vegetableArrayListFoes.clear();
            ListenerBeneFoes(subFoes);
        }

        recyclerViewFoes.setAdapter(myAdapterFoes);
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
                                vegetableArrayListFoes.add(dc.getDocument().toObject(vegetable.class));
                            }
                            myAdapterFoes.notifyDataSetChanged();
                        }
                        if (vegetableArrayListFoes.isEmpty()){
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
//        i.putExtra("key", vegetableArrayListFoes.get(NamePos).getName());
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
        String getV = vegetableArrayListFoes.get(GetPos).getName();
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
            OpenTab(getV);
        } else {
            Toast.makeText(getActivity(), "Not Available", Toast.LENGTH_SHORT).show();
        }
    }
}