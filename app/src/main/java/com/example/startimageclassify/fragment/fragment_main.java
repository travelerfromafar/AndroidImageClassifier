package com.example.startimageclassify.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.startimageclassify.R;
import com.example.startimageclassify.classes.Spacing;
import com.example.startimageclassify.classes.vegetable;
import com.example.startimageclassify.myAdapterBeneFoes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_main extends AppCompatActivity implements
        fragment_bene.UsersProvider,
        fragment_foe.UsersProvider,
        fragment_info.UsersProvider{

//    SharedViewModel viewModel;
    TabLayout tabLayout;
    ViewPager viewPager;

    private static final String FRAGMENT_TAG = "fragment_bene";
    private Fragment fragment;

//    Button first_Frag, second_frag;
    Boolean IsAtFav = false;
    ImageButton btnAddFav;
    String value, DocuPath, subBene, subFoes, getPath;
    ImageView VegeImageView;
    TextView fragmentText;
    BottomNavigationView bottomNav;
//    public static final String TAG = "myLogs";

//    RecyclerView recyclerViewBene, recyclerViewFoes;
    ArrayList<vegetable> vegetableArrayListBene, vegetableArrayListFoes;
    ArrayList<Spacing> spacingArrayList = new ArrayList<>();
    myAdapterBeneFoes myAdapterBenefits, myAdapterFoes;
//    FrameLayout layout = new FrameLayout(this);

    public FirebaseFirestore db;
    FirebaseAuth CurAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

//        if (null == savedInstanceState) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Layout,
//                    new fragment_bene()).commit();
//        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
          value = extras.getString("key");
        }

//        SetUpInfoFor();
        DocuPath = value;
        CurAuth = FirebaseAuth.getInstance();


        replaceFragment(new fragment_bene());

        VegeImageView = findViewById(R.id.image_header);
        fragmentText = findViewById(R.id.FragVegetableName);
        bottomNav = findViewById(R.id.frag_button_nav);
        btnAddFav = findViewById(R.id.btnAddFavRemove);

//        VegeImageView.setImageURI(Uri.parse("https://console.firebase.google.com/u/0/project/imageclassify-4d882/storage/imageclassify-4d882.appspot.com/files/~2Fvegetables"));
        fragmentText.setText(value);
        ImageSetUp(value);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {

                    case R.id.nav_bene:
                        selectedFragment = new fragment_bene();
                        break;

                    case R.id.nav_foes:
                        selectedFragment = new fragment_foe();
                        break;

                    case R.id.nav_info:
                        selectedFragment = new fragment_info();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Layout, selectedFragment).commit();
                }
                return true;
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            CheckIfinFB(DocuPath);

            btnAddFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (IsAtFav){
                        RemoveToFavFB(DocuPath);
                    } else {
                        AddToFavFB(DocuPath);
                    }
                }
            });
        } else {
            btnAddFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(fragment_main.this,"You are not logged in", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Layout,
//                    new fragment_bene()).commit();
//        }

//        first_Frag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { replaceFragment(new fragment_bene()); }
//        });
//
//        second_frag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                replaceFragment(new fragment_foe());
//            }
//        });
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_Layout, fragment);
        fragmentTransaction.commit();
    }

    private void AddToFavFB(String CurVeg) {
//        String[] VegeName = getResources().getStringArray(R.array.class_1_tomato);
//        String[] VegeSpacing = getResources().getStringArray(R.array.class_1_tomato_spacing);

//        String[] VegeName = {"one","two","three"};
//        String[] VegeSpacing = {"1 - 2 ", " 39 -2 -", "2 - 0"};
//        for (int i = 0; i < VegeName.length; i++){
//            spacingArrayList.add(new Spacing(VegeName[i], VegeSpacing[i]));
//        }
        CurAuth = FirebaseAuth.getInstance();
        if (CurAuth.getCurrentUser() == null){
            Toast.makeText(fragment_main.this,"You are not logged in", Toast.LENGTH_SHORT).show();
        }
        else {
            long timestamp = System.currentTimeMillis();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", ""+CurVeg);
            hashMap.put("filclass",""+ClassSetUp(CurVeg));
//            hashMap.put("image", ""+)

            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Users");
            dataref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(fragment_main.this,"Added to Favorites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(fragment_main.this,"Failed to add due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void RemoveToFavFB(String CurVeg) {
//        String[] VegeName = getResources().getStringArray(R.array.class_1_tomato);
//        String[] VegeSpacing = getResources().getStringArray(R.array.class_1_tomato_spacing);

//        String[] VegeName = {"one","two","three"};
//        String[] VegeSpacing = {"1 - 2 ", " 39 -2 -", "2 - 0"};
//        for (int i = 0; i < VegeName.length; i++){
//            spacingArrayList.add(new Spacing(VegeName[i], VegeSpacing[i]));
//        }
        CurAuth = FirebaseAuth.getInstance();
//        if (CurAuth.getCurrentUser() == null){
//            Toast.makeText(fragment_main.this,"You are not logged in", Toast.LENGTH_SHORT).show();
//        }
//        else {

            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Users");
            dataref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(fragment_main.this,"Removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(fragment_main.this,"Failed to remove due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//        }

    }

    private void CheckIfinFB(String CurVeg){
        if (CurVeg == null) {
            Toast.makeText(fragment_main.this,"NO USER", Toast.LENGTH_SHORT).show();

        } else {
            DatabaseReference chref = FirebaseDatabase.getInstance().getReference("Users");
            chref.child(CurAuth.getUid()).child("Favorites").child(CurVeg)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            IsAtFav = snapshot.exists();
                            if (IsAtFav) {
                                btnAddFav.setImageResource(R.drawable.ic_remove_fav);
                                btnAddFav.getBackground().setTint(getResources().getColor(R.color.button_color_negative));
                            } else {
                                btnAddFav.setImageResource(R.drawable.ic_add_fav);
                                btnAddFav.getBackground().setTint(getResources().getColor(R.color.button_color_background));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

//    public void ListenerBeneFoes(String sub, String Docu) {
//        db.collection("Vegetables").document(Docu).collection(sub)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//                            Log.e("Firestore now working", error.getMessage());
//                            return;
//                        } else if (sub.matches("Benefits")) {
//                            for (DocumentChange dc : value.getDocumentChanges()) {
//                                if (dc.getType() == DocumentChange.Type.ADDED) {
//                                    vegetableArrayListBene.add(dc.getDocument().toObject(vegetable.class));
//                                }
////                                myAdapterBenefits.notifyDataSetChanged();
////                                if (progressDialog.isShowing())
////                                    progressDialog.dismiss();
//                            }
//                        } else if (sub.matches("Foes")) {
//                            for (DocumentChange dc : value.getDocumentChanges()) {
//                                if (dc.getType() == DocumentChange.Type.ADDED) {
//                                    vegetableArrayListFoes.add(dc.getDocument().toObject(vegetable.class));
//                                }
////                                myAdapterFoes.notifyDataSetChanged();
////                                if (progressDialog.isShowing())
////                                    progressDialog.dismiss();
//                            }
//                        }
//                    }
//                });
//    }

    private void ImageSetUp (String valImage){
        switch (valImage) {
            case "Bitter":
                fragmentText.setText("Bitter Gourd");
                VegeImageView.setImageResource(R.drawable.veg_img_bitter);
                break;
            case "Cabbage":
                VegeImageView.setImageResource(R.drawable.veg_img_cab);
                break;
            case "Chili":
                VegeImageView.setImageResource(R.drawable.veg_img_chili);
                break;
            case "Eggplant":
                VegeImageView.setImageResource(R.drawable.veg_img_eggp);
                break;
            case "Ginger":
                VegeImageView.setImageResource(R.drawable.veg_img_gin);
                break;
            case "Lettuce":
                VegeImageView.setImageResource(R.drawable.veg_img_lett);
                break;
            case "Onion":
                VegeImageView.setImageResource(R.drawable.veg_img_oni);
                break;
            case "Pechay":
                VegeImageView.setImageResource(R.drawable.veg_img_bokchoy);
                break;
            case "Sweet Potato":
                VegeImageView.setImageResource(R.drawable.veg_img_swepo);
                break;
            case "Tomato":
                VegeImageView.setImageResource(R.drawable.veg_img_toma);
                break;

            default: VegeImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private String ClassSetUp (String valClass){
        String SetClass = null;
        String[] FilClass = getResources().getStringArray(R.array.vegeTableClass);
        switch (valClass) {
            case "Bitter":
                SetClass = FilClass[0];
                break;
            case "Cabbage":
                SetClass = FilClass[1];
                break;
            case "Chili":
                SetClass = FilClass[2];
                break;
            case "Eggplant":
                SetClass = FilClass[3];
                break;
            case "Ginger":
                SetClass = FilClass[4];
                break;
            case "Lettuce":
                SetClass = FilClass[5];
                break;
            case "Onion":
                SetClass = FilClass[6];
                break;
            case "Pechay":
                SetClass = FilClass[7];
                break;
            case "Sweet Potato":
                SetClass = FilClass[8];
                break;
            case "Tomato":
                SetClass = FilClass[9];
                break;

            default: VegeImageView.setImageResource(R.drawable.ic_launcher_background);
        }
        return SetClass;
    }

    public ArrayList<vegetable> getVegsBene() {
        return vegetableArrayListBene;
    }

    public ArrayList<vegetable> getVegsFoes() {
        return vegetableArrayListFoes;
    }

    public ArrayList<Spacing> getSpacing() { return spacingArrayList; }

    public String getVegsName() {
        return DocuPath;
    }

}