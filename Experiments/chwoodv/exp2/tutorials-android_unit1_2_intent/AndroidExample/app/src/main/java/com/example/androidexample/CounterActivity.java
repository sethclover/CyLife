package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView messageText;
    private TextView numberTxt; // define number textview variable
    private Button increaseBtn; // define increase button variable
    private Button decreaseBtn; // define decrease button variable
    private Button backBtn;     // define back button variable

    private int counter = 1;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        messageText = findViewById(R.id.keepText);
        numberTxt = findViewById(R.id.number);
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);

        Bundle extras = getIntent().getExtras();
        String newText = extras.getString("TEXT");  // this will come from Main
        messageText.setText(newText);

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter*=2; numberTxt.setText(String.valueOf(Math.max(0, counter)));
                if (counter <= 0) {
                    counter = 0;
                    numberTxt.setTextSize(150);
                }
                else if (counter > 100000000) {
                    numberTxt.setTextSize(70);
                }
                else if (counter > 10000000) {
                    numberTxt.setTextSize(75);
                }
                else if (counter > 1000000) {
                    numberTxt.setTextSize(80);
                }
                else if (counter > 100000) {
                    numberTxt.setTextSize(95);
                }
                else if (counter > 10000) {
                    numberTxt.setTextSize(120);
                }

            }

        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter /= 2; numberTxt.setText(String.valueOf(counter));
                if (counter < 10000) {
                    numberTxt.setTextSize(150);
                }
                else if (counter < 100000) {
                    numberTxt.setTextSize(120);
                }
                else if (counter < 1000000) {
                    numberTxt.setTextSize(95);
                }
                else if (counter < 10000000) {
                    numberTxt.setTextSize(80);
                }
                else if (counter < 100000000) {
                    numberTxt.setTextSize(75);
                }
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

    }
}