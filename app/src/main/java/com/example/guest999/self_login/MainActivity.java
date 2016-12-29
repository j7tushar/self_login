package com.example.guest999.self_login;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private EditText ed_n, ed_no, ed_pass;
    private Button btn_s;
    public String sn, sno, spass;
    RequestQueue requestQueue;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadUiElements();
        LoadUiListener();
    }

    private void LoadUiListener() {
        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
    }

    private void LoadUiElements() {
        ed_n = (EditText) findViewById(R.id.ed_username);
        ed_no = (EditText) findViewById(R.id.ed_userno);
        ed_pass = (EditText) findViewById(R.id.ed_pass);
        textView = (TextView) findViewById(R.id.tv_login);

        btn_s = (Button) findViewById(R.id.btn_submit);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
    }

    public void Register() {
        final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "Processing", "Please wait...", false, false);
        sn = ed_n.getText().toString();
        sno = ed_no.getText().toString();
        spass = ed_pass.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_REGISTER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse: ", response + "");
                        if (response.equalsIgnoreCase("success")) {
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "Done..!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            finish();
                            startActivity(intent);

                        } else {
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "Already Register", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(MainActivity.this, "Fail..!", Toast.LENGTH_LONG).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_USERNAME, sn);
                params.put(Config.KEY_USEREMAIL, sno);
                params.put(Config.KEY_USERPASS, spass);
                Log.e("getParams: ", params + "");
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}
