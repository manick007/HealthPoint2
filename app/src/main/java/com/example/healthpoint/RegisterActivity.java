package com.example.healthpoint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPass;
    private EditText regConfimPass;
    private Button regBtn;
    private Button regLoginBtn;
    private ProgressBar regProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        regEmail = (EditText) findViewById(R.id.regemail);
        regPass = (EditText) findViewById(R.id.regpass);
        regConfimPass = (EditText) findViewById(R.id.regconfimpass);
        regBtn = (Button) findViewById(R.id.regbtn);
        regLoginBtn = (Button) findViewById(R.id.regloginbtn);
        regProgress = (ProgressBar) findViewById(R.id.regprogress);

        regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMain();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = regEmail.getText().toString();
                String pass = regPass.getText().toString();
                String confim_pass = regConfimPass.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

                    if (pass.equals(confim_pass)){
                        regProgress.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    sendToMain();

                                }else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Erreur: " + errorMessage, Toast.LENGTH_LONG).show();

                                }

                                regProgress.setVisibility(View.INVISIBLE);
                            }
                        });



                    }else {

                        Toast.makeText(RegisterActivity.this, "Le mot de passe et sa confirmation ne sont pas identiques. ", Toast.LENGTH_LONG).show();

                    }
                }

            }
        });

    }

    @Override
    protected void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            
            sendToMain();
            
        }

        super.onStart();
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
