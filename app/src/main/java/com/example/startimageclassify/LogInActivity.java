package com.example.startimageclassify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    Button btnReg, btnLog, btnFor;
//    EditText etemail;
//    EditText etpass;
    boolean shps;
    com.google.android.material.textfield.TextInputLayout etpassLY, etemailLY;
    com.google.android.material.textfield.TextInputEditText etemail, etpass;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btnReg = findViewById(R.id.Reg_Comp);
        btnLog = findViewById(R.id.Log_Comp);

        etemail = findViewById(R.id.email_in);
        etpass = findViewById(R.id.pass_in);
        etemailLY = findViewById(R.id.email_inLY);
        etpassLY = findViewById(R.id.pass_inLY);
//        etcustomer = findViewById(R.id.til_customer_no);
//        etcutomer = findViewById(R.id.et_customer_no);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);

//        etpass.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int Right = 2;
//                if (event.getAction()==MotionEvent.ACTION_UP){
//                    if (event.getRawX()>=etpass.getRight()-etpass.getCompoundDrawables()[Right].getBounds().width()){
//                        int selection = etpass.getSelectionEnd();
//                        if (shps){
//                            etpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_notshowps, 0);
//                            etpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                            shps = false;
//                        } else {
//                            etpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_showps, 0);
//                            etpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                            shps = true;
//                        }
//                        etpass.setSelection(selection);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Logging...");
                progressDialog.show();
                UserLog();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void UserLog() {
        String emcheck = etemail.getText().toString().trim();
        String pscheck = etpass.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(emcheck,pscheck).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    EndProgLoad();
                } else {
                    Toast.makeText(LogInActivity.this, "Failed to login user", Toast.LENGTH_LONG).show();
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