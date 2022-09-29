package com.example.startimageclassify;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.startimageclassify.classes.vegetable;
import com.example.startimageclassify.fragment.fragment_main;
import com.example.startimageclassify.vegetable.ListOfVegetables;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //recyclerview
    RecyclerView recyclerView, recyclerViewFoes;
    ArrayList<vegetable> vegetableArrayListBene, vegetableArrayListFoes;
    myAdapterBeneFoes myAdapterBenefits, myAdapterFoes;

    //test database
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    final Context context = this;
    private Button btnList, btncamera, btngallery, btnabt, btnFav;
    private ImageButton btnLogNOut;
    EditText getVegetable;
    String DocumentPath, subBene, subFoes, getPath;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subBene = "Benefits";
        subFoes = "Foes";

//        btnbutton = findViewById(R.id.btnbutton);
        btnList = findViewById(R.id.btnList);
        btncamera = findViewById(R.id.camera_button);
        btngallery = findViewById(R.id.gallery_button);
        btnFav = findViewById(R.id.btnFav);
        btnabt = findViewById(R.id.about_button);

        btnLogNOut = findViewById(R.id.btnLog);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            btnLogNOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            btnLogNOut.setImageResource(R.drawable.ic_login);
            btnLogNOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                }
            });
        }

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity();
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Login is required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    public void OpenActivity() {
        Intent intent = new Intent(this, ListOfVegetables.class);
        startActivity(intent);
    }

    public void OpenCamera() {
        Intent intent = new Intent(this, camera.class);
        startActivity(intent);
    }


    public void Checker() {
        String getV = getVegetable.getText().toString();
        if (getV.matches("")) {
            Toast.makeText(this, "Not Applicable", Toast.LENGTH_SHORT).show();
        } else if (getV.equalsIgnoreCase("carrot")) {
            DocumentGet(getPath = getV);
        } else if (getV.equalsIgnoreCase("radish")) {
            DocumentGet(getPath = getV);
        } else if (getV.equalsIgnoreCase("spinach")) {
            DocumentGet(getPath = getV);
        } else if (getV.equalsIgnoreCase("Tomato")) {
            DocumentGet(getPath = getV);
        } else {
            Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                Intent i = new Intent(MainActivity.this, camera.class);
                i.putExtra("Bitmap", image);
                startActivity(i);
//                int dimension = Math.min(image.getWidth(), image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//                imageView.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
//                Checker();
            }else{
                Uri dat = data.getData();
//                Bitmap image = null;
//                try {
//                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Bitmap bitGal = image;
//                bitGal = (Bitmap) data.getExtras().get("data");
                Intent i = new Intent(MainActivity.this, camera.class);
                i.putExtra("galleryImage", dat.toString());
                startActivity(i);

//                imageView.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void OpenTab(String NameRes) {
        Intent i = new Intent(MainActivity.this, fragment_main.class);
        i.putExtra("main_act", NameRes);
        startActivity(i);
    }

    private void DocumentGet(String path) {
        DocumentPath = path;
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();
        OpenTab(DocumentPath);

//        if (progressDialog.isShowing())
//            progressDialog.dismiss();
//        vegetableArrayListBene.clear();
//        ListenerBeneFoes(subBene);
//
//        vegetableArrayListFoes.clear();
//        ListenerBeneFoes(subFoes);
    }

    private void ListenerBeneFoes(String sub) {
        String subRes = sub;
        db.collection("Vegetables").document(DocumentPath).collection(sub)
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

//    private void ListenerChanger() {     //Benefits
//        db.collection("Vegetables").document(DocuPath).collection(subBene)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//                            Log.e("Firestore now working", error.getMessage());
//                            return;
//                        }
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                vegetableArrayListBene.add(dc.getDocument().toObject(vegetable.class));
//                            }
//                            myAdapterBenefits.notifyDataSetChanged();
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//                        }
//                    }
//                });
//    }
//
//    private void ListenerChangerFoes() {     //Foes
//        db.collection("Vegetables").document(DocuPath).collection(subFoes)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//                            Log.e("Firestore now working", error.getMessage());
//                            return;
//                        }
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                vegetableArrayListFoes.add(dc.getDocument().toObject(vegetable.class));
//                            }
//                            myAdapterFoes.notifyDataSetChanged();
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//                        }
//                    }
//                });
//    }
//}


//    private void InsertData() {
//        String vegetablename = name.getText().toString();
//        String vegetabledesc = description.getText().toString();
//        String Id = databaseVegetable.push().getKey();
//
//        vegetable vegetable = new vegetable(vegetablename, vegetabledesc);
//        databaseVegetable.child("vegetables").child(Id).setValue(vegetable)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(MainActivity.this, "Vegetable Details Inserted", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }