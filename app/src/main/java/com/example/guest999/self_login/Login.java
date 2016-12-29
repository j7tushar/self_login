package com.example.guest999.self_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText ed_email, ed_pass;
    private Button btn_login;
    private TextView textView;
    private String s_email, s_pass;

    RequestQueue requestQueue;

    public static final String PREF_NAME = "clear";
    public static boolean hasLoggedIn;
    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(PREF_NAME, 0);
        hasLoggedIn = preferences.getBoolean("hasLoggedIn", false);

        if (hasLoggedIn) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        }
        LoadUiElements();
        LoadUiListener();
    }

    private void LoadUiListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }

    private void LoadUiElements() {

        ed_email = (EditText) findViewById(R.id.ed_e);
        ed_pass = (EditText) findViewById(R.id.ed_pass);

        textView = (TextView) findViewById(R.id.tv_login);

        btn_login = (Button) findViewById(R.id.btn_login);

        requestQueue = Volley.newRequestQueue(Login.this);
    }

    private void Submit() {

        s_email = ed_email.getText().toString();
        s_pass = ed_pass.getText().toString();
        final ProgressDialog loading = ProgressDialog.show(Login.this, "Processing", "Please Wait", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("onResponse: ", response + "");
                        if (response.equalsIgnoreCase("success")) {

                            SharedPreference.setDefaults("email", s_email, getApplicationContext());

                            SharedPreference.setDefaultboolean("hasLoggedIn", true, getApplicationContext());
                            SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("email", s_email);
                            editor.putBoolean("hasLoggedIn", true);
                            editor.apply();

                            Toast.makeText(Login.this, "Done..!", Toast.LENGTH_LONG).show();
                            Log.e( "onResponse: ",SharedPreference.getDefaults("email",getApplicationContext()) );
                            Intent intent = new Intent(Login.this, Home.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Need to Register First..", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(Login.this, "Fail..!", Toast.LENGTH_LONG).show();

                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_USEREMAIL, s_email);
                params.put(Config.KEY_USERPASS, s_pass);

                Log.e("getParams: ", params + "");

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


}
