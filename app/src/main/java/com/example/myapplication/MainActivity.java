package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private TextView data;
    private SwipeRefreshLayout swipe;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "https://api.kawalcorona.com/indonesia/";

        data = findViewById(R.id.data);
        swipe = findViewById(R.id.swipe);

        requestQueue = Volley.newRequestQueue(this);

        getRequest(url);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequest(url);
            }
        });
    }

    public void getRequest(String url) {
        swipe.setRefreshing(true);
        JsonArrayRequest get = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        data.setText("Negara : " + obj.getString("name") + "\n\n\n Positif : " + obj.getString("positif") + "\n\n\nSembuh : " + obj.getString("sembuh") + "\n\n\n Meninggal : " + obj.getString("meninggal"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipe.setRefreshing(false);
                }
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Kesalahan Koneksi", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });

        requestQueue.add(get);
    }
}
