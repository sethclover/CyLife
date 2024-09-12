package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        messageText = findViewById(R.id.main_msg_txt);
        messageText.setText("Namaste Ya'll");

// Create and apply a fade-in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(5000); // Duration in milliseconds
        fadeIn.setFillAfter(true);
        messageText.startAnimation(fadeIn);

    }
}

