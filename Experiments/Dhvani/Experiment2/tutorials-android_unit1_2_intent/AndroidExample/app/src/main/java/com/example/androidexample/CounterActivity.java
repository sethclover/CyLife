package com.example.androidexample;

import static com.example.androidexample.R.id.counter_increase_btn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button Multiply; // define increase button variable
    private Button Divideb; // define decrease button variable
    private Button backBtn;     // define back button variable

    private int counter = 1;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        Multiply = findViewById(counter_increase_btn);
        Divideb = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);

        /* when increase btn is pressed, counter++, reset number textview */
        Multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(2*counter));
                counter = counter*2;
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        Divideb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter/2));
                counter = counter/2;
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}