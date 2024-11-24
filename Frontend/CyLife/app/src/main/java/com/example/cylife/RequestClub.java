package com.example.cylife;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestClub extends AppCompatActivity {

  private EditText etClubName, etEmail, etClubDesc;
  private Button createClubButton;
  private TextView clubCreationStatus;
  private RequestQueue requestQueue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_request_club);

    etClubName = findViewById(R.id.etClubName);
    etEmail = findViewById(R.id.etEmail);
    etClubDesc = findViewById(R.id.etClubDesc);
    createClubButton = findViewById(R.id.createClubButton);
    clubCreationStatus = findViewById(R.id.clubCreationStatus);

    requestQueue = Volley.newRequestQueue(this);

    createClubButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            createClub();
          }
        });
  }

  private void createClub() {
    String url = "http://coms-3090-065.class.las.iastate.edu:8080/club-requests";

    String clubName = etClubName.getText().toString();
    String email = etEmail.getText().toString();
    String clubDesc = etClubDesc.getText().toString();

    if (clubName.isEmpty() || email.isEmpty() || clubDesc.isEmpty()) {
      clubCreationStatus.setText("All fields are required.");
      return;
    }

    JSONObject postData = new JSONObject();
    try {
      postData.put("clubName", clubName);
      postData.put("clubEmail", email);
      postData.put("description", clubDesc);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    StringRequest stringRequest =
        new StringRequest(
            Request.Method.POST,
            url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                clubCreationStatus.setText("Club request successfully sent!");
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                clubCreationStatus.setText("Error creating club: " + error.getMessage());
              }
            }) {
          @Override
          public byte[] getBody() {
            return postData.toString().getBytes();
          }

          @Override
          public String getBodyContentType() {
            return "application/json; charset=utf-8";
          }
        };

    requestQueue.add(stringRequest);
  }
}
