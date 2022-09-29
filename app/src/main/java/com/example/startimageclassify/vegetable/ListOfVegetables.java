package com.example.startimageclassify.vegetable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.startimageclassify.R;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.SearchView;

import com.example.startimageclassify.classes.ItemClick;
import com.example.startimageclassify.classes.ListRecyclerVeg;
import com.example.startimageclassify.classes.listvegs;
import com.example.startimageclassify.databinding.ActivityListOfVegetablesBinding;
import com.example.startimageclassify.fragment.fragment_main;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class ListOfVegetables extends AppCompatActivity implements ItemClick {

    ActivityListOfVegetablesBinding binding;
    SearchView searchView;

    public ArrayList<listvegs> vegsArrayList = new ArrayList<>();
    ListRecyclerVeg listRecycler;
    RecyclerView recyclerViewList;
    FirebaseFirestore db;
//    DatabaseReference dataref;
//    Boolean tickClick = false;

    String selectedFilter = "all", currentSearchText = "";

    @Override
    public void onItemClick(int position) {
        String getV = vegsArrayList.get(position).getName();
//        int getIm = vegsArrayList.get(position).getImage();
        OpenTab(getV);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivityListOfVegetablesBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_list_of_vegetables);
        setContentView(R.layout.activity_list_of_vegetables);


        recyclerViewList = findViewById(R.id.RecyclerList);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(ListOfVegetables.this));

        int[] imageId = {R.drawable.veg_img_bitter, R.drawable.veg_img_cab,
                        R.drawable.veg_img_chili, R.drawable.veg_img_eggp,
                        R.drawable.veg_img_gin, R.drawable.veg_img_lett,
                        R.drawable.veg_img_oni, R.drawable.veg_img_bokchoy,
                        R.drawable.veg_img_swepo, R.drawable.veg_img_toma};

        String[] vegName = getResources().getStringArray(R.array.vegeTableList);
        String[] FilClass = getResources().getStringArray(R.array.vegeTableClass);
        for(int i = 0; i < imageId.length; i++){
            listvegs vegs = new listvegs(vegName[i], FilClass[i], imageId[i]);

            vegsArrayList.add(vegs);
        }
        listRecycler = new ListRecyclerVeg(ListOfVegetables.this, vegsArrayList, this);

//        db = FirebaseFirestore.getInstance();
//        db.collection("vegetables_of").orderBy("name", Query.Direction.ASCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                vegsArrayList.add(dc.getDocument().toObject(listvegs.class));
//                            }
//                            listRecycler.notifyDataSetChanged();
//                        }
//                    }
//                });

        recyclerViewList.setAdapter(listRecycler);

        SearchInList();
//        ListAdapter listAdapter = new ListAdapter(ListOfVegetables.this, vegsArrayList);
//
//        SearchInList();
//        binding.listview.setAdapter(listAdapter);
//        binding.listview.setClickable(true);
//        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                if (position==0){
////                    OpenTab(vegetablePass = "Carrot");
////                } else if (position==1){
////                    OpenTab(vegetablePass = "Radish");
////                } else if (position==2){
////                    OpenTab(vegetablePass = "Spinach");
////                }
//                OpenTab(vegName[position]);
//            }
//        });

//        ListView listView = findViewById(R.id.listview);
//
//        List<String> list = new ArrayList<>();
//        list.add("Carrot");
//        list.add("Radish");
//        list.add("Spinach");
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,list);
//        listView.setAdapter(arrayAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position==0){
//                    OpenTab(vegetablePass = "Carrot");
//                } else if (position==1){
//                    OpenTab(vegetablePass = "Radish");
//                } else if (position==2){
//                    OpenTab(vegetablePass = "Spinach");
//                }
//            }
//        });
    }

    private void SearchInList() {
        searchView = findViewById(R.id.SearchList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listRecycler.getFilter().filter(newText);
//
//                ArrayFunc(newText);

//                currentSearchText = newText;
//                ArrayList<ListVegs> filteredList = new ArrayList<ListVegs>();
//
//                for(ListVegs vegs: vegsArrayList){
//                    if (vegs.getVegName().toLowerCase().contains(newText.toLowerCase(Locale.ROOT))){
//                        if(selectedFilter.equals("all")){
//                            filteredList.add(vegs);
//                        } else {
//                            if (vegs.getVegName().toLowerCase().contains(selectedFilter)){
//                                filteredList.add(vegs);
//                            }
//                        }
//                    }
//                }

//                ListAdapter adapter = new ListAdapter(ListOfVegetables.this, filteredList);
//                binding.listview.setAdapter(adapter);

                return false;
            }
        });
    }

    private void OpenTab(String NameRes) {
        Intent i = new Intent(ListOfVegetables.this, fragment_main.class);
        i.putExtra("key", NameRes);
        startActivity(i);
    }

    private void FilterList (String status) {

        selectedFilter = status;
//        ArrayFunc(status);
        ArrayList<listvegs> filteredList = new ArrayList<listvegs>();
        for(listvegs vegs: vegsArrayList){
            if (vegs.getFilclass().toLowerCase().contains(status)){
                if (currentSearchText.equals("")){
                    filteredList.add(vegs);
                } else {
                    if (vegs.getFilclass().toLowerCase().contains(currentSearchText.toLowerCase(Locale.ROOT))){
                        filteredList.add(vegs);
                    }
                }
            }
        }

//        ListAdapter adapter = new ListAdapter(ListOfVegetables.this, filteredList);
//        binding.listview.setAdapter(adapter);
    }

//    private void ArrayFunc(String text){
//        ArrayList<ListVegs> filteredList = new ArrayList<>();
//        for(ListVegs vegs: vegsArrayList){
//            if (vegs.getFilClass().toLowerCase().contains(text.toLowerCase(Locale.ROOT))){
//                filteredList.add(vegs);
//            }
//        }
//
//        ListAdapter adapter = new ListAdapter(ListOfVegetables.this, filteredList);
//        binding.listview.setAdapter(adapter);
//    }
    public void FifthFilter(View view) {
        selectedFilter = "all";
        searchView.setQuery("",false);
        searchView.clearFocus();

//        ListAdapter adapter = new ListAdapter(ListOfVegetables.this, vegsArrayList);
//        binding.listview.setAdapter(adapter);
    }

    public ArrayList<listvegs> getVegsFoes() {
        return vegsArrayList;
    }

    public void FirstFilter(View view) {
        FilterList("green");
    }

    public void SecondFilter(View view) {
        FilterList("leaves");
    }

    public void ThirdFilter(View view) {
        FilterList("water");
    }

    public void FourthFilter(View view) {
        FilterList("fire");
    }


}