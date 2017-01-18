package com.example.test.layarkita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dhika TB on 27/07/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText username,email,password;
    private Button regBtn;
    private RequestQueue requestQueue;
    private static final String URL =  "http://192.168.201.1/layarkita/user_control.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email =(EditText) findViewById(R.id.email);
        password =(EditText) findViewById(R.id.password);
        username =(EditText) findViewById(R.id.username);
        regBtn = (Button) findViewById(R.id.regBtn);

        requestQueue = Volley.newRequestQueue(this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cekEmail = email.getText().toString();
                final String cekUser = username.getText().toString();
                if (!isValidEmail(cekEmail)) {
                    email.setError("Email Tidak Benar");
                } else if (cekUser.length() < 3) {
                    username.setError("Username terlalu pendek");
                } else {
                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(RegisterActivity.this, jsonObject.getString("success"), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(RegisterActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("username", username.getText().toString());
                            hashMap.put("email", email.getText().toString());
                            hashMap.put("password", password.getText().toString());
                            return hashMap;
                        }
                    };
                    requestQueue.add(request);
                }
            }
        });



    }

    private boolean isValidEmail(String cekEmail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(cekEmail);
        return matcher.matches();
    }
}
