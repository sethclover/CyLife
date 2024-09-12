package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView msg;
    private TextView User;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        User = findViewById(R.id.main_username_txt);// link to username textview in the Main activity XML
        signup = findViewById(R.id.main_signup_btn);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            msg.setText("Home Page");
            User.setVisibility(View.INVISIBLE);
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(MainActivity.this, signupAct.class);
                startActivity(intent);
            }
        });

        ImageButton fullscreenButton = findViewById(R.id.fullscreen_button);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }


        });
    }
}
