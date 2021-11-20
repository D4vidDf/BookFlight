package com.d4viddf.bookflight.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.d4viddf.bookflight.Bottom;
import com.d4viddf.bookflight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadingApp extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app);
        View view = new View(this);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser !=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(LoadingApp.this, Bottom.class);
                    LoadingApp.this.startActivity(mainIntent);
                    LoadingApp.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(LoadingApp.this, LoginActivity.class);
                    LoadingApp.this.startActivity(mainIntent);
                    LoadingApp.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}