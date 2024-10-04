package com.example.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameField, passwordField, confirmPasswordField;
    private CheckBox internationalCheckbox, multiculturalCheckbox;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        internationalCheckbox = findViewById(R.id.international_checkbox);
        multiculturalCheckbox = findViewById(R.id.multicultural_checkbox);
        signupButton = findViewById(R.id.signup_button);

        // Handle sign-up logic
        signupButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();
            boolean isInternational = internationalCheckbox.isChecked();
            boolean isMulticultural = multiculturalCheckbox.isChecked();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Process the form data (e.g., send to backend or save locally)
                Toast.makeText(SignupActivity.this,
                        "Sign-up successful! International: " + isInternational + ", Multicultural: " + isMulticultural,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

