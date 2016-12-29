package com.example.guest999.self_login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    HomeAdapter adapter;
    public static ArrayList<HashMap<String, String>> Joshi = null;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);

        LoadData();
    }

    private void LoadData() {
        Joshi = new ArrayList<HashMap<String, String>>();
        final ProgressDialog dialog = ProgressDialog.show(this, "Processing", "Please Wait..", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_CITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("onResponse: ", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("City");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String name = jsonObject1.getString(Config.TAG_CITY_NAME);
                                String image = jsonObject1.getString(Config.TAG_CITY_IMAGE);

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(Config.TAG_CITY_NAME, name);
                                map.put(Config.TAG_CITY_IMAGE, image);

                                Joshi.add(map);
                                IntialAdapter();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(Home.this, "Internet Connection Low..", Toast.LENGTH_LONG).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void IntialAdapter() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(Home.this, Joshi);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Logout() {
        new AlertDialog.Builder(Home.this)
                .setTitle("Logout")
                .setMessage("Sure Logout?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreference.setDefaultboolean("hasLoggedIn", false, Home.this);
                        SharedPreference.setDefaults("email", null, Home.this);
                        Login.preferences.edit().clear().apply();
                        SharedPreference.Clear(Home.this);
                        Intent intent = new Intent(Home.this, Login.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();

    }

}
