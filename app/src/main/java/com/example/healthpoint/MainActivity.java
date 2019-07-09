package com.example.healthpoint;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private int progressStatus = 0;
    private Toolbar mainToolbar;
    private ProgressBar mainProgress;
    private Button mainBtn;
    private TextView mainCount;
    private Handler handler = new Handler();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mainCount = (TextView) findViewById(R.id.maincount);
        mainBtn = (Button) findViewById(R.id.mainbutton);
        mainProgress = (ProgressBar) findViewById(R.id.mainprogress);
        mainToolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Health Point");


        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the progress status zero on each button click
                progressStatus = 0;
                // Start the lengthy operation in a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(progressStatus < 21){
                            // Update the progress status
                            progressStatus +=1;

                            // Try to sleep the thread for 20 milliseconds
                            try{
                                Thread.sleep(20);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mainProgress.setProgress(progressStatus);
                                    // Show the progress on TextView
                                    mainCount.setText(progressStatus+"");
                                }
                            });
                        }
                    }
                }).start(); // Start the operation
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

         if (currentUser == null){

             sendToLogin();

         }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.mainlogout:
                logOut();
                return true;

                default:
                    return false;
        }
    }

    private void logOut() {

        mAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
