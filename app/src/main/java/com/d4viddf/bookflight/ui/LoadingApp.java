package com.d4viddf.bookflight.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.d4viddf.bookflight.MainActivity;
import com.d4viddf.bookflight.R;

public class LoadingApp extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app);
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