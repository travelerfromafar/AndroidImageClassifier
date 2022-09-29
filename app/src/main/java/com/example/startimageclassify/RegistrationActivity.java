package com.example.startimageclassify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startimageclassify.classes.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText etuname, etfname, etemail, etpass;
    Button btnReg;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        etuname = findViewById(R.id.uname_in);
        etfname = findViewById(R.id.fname_in);
        etemail = findViewById(R.id.email_in);
        etpass = findViewById(R.id.pass_in);

        btnReg = findViewById(R.id.Reg_Btn_reg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Registering...");
                progressDialog.show();
                valReg();
            }
        });
    }

    private void valReg () {
        String emcheck = etemail.getText().toString().trim();
        String pscheck = etpass.getText().toString().trim();
        String uncheck = etuname.getText().toString().trim();
        String fncheck = etfname.getText().toString().trim();

        if (fncheck.isEmpty()){
            etfname.setError("Name is required");
            etfname.requestFocus();
            EndProgLoad();
            return;
        }
        if (uncheck.isEmpty()){
            etuname.setError("Username is required");
            etuname.requestFocus();
            EndProgLoad();
            return;
        }
        if (emcheck.isEmpty()){
            etemail.setError("Email is required");
            etemail.requestFocus();
            EndProgLoad();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emcheck).matches()){
            etemail.setError("Email is not valid");
            etemail.requestFocus();
            EndProgLoad();
            return;
        }
        if (pscheck.isEmpty()){
            etpass.setError("Password is required");
            etpass.requestFocus();
            EndProgLoad();
            return;
        }
        if (pscheck.length() < 8){
            etpass.setError("Min Password length is 8 characters");
            etpass.requestFocus();
            EndProgLoad();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emcheck,pscheck)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            UserObj user = new UserObj(uncheck, fncheck, emcheck);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                Toast.makeText(RegistrationActivity.this, "User has been added successfully", Toast.LENGTH_SHORT).show();
                                                EndProgLoad();
                                            } else {
                                                Toast.makeText(RegistrationActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                                                EndProgLoad();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                            EndProgLoad();
                        }
                    }
                });
    }

    private void EndProgLoad(){
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
}