package com.example.assighnment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Joke extends AppCompatActivity {
    private TextView joke;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    final static String key = "joke";

    final static String X_API_KEY = "fUR1UVPNVZQVfdZtVh9Zkb5FtRBb8JwhwlON68lz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        joke = findViewById(R.id.joke);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        if (editor != null) {
            if (loadIN() != null && !loadIN().isEmpty()) {
                joke.setText(loadIN());
            } else {
                generateAjoke();
            }
        }
        //when user press the backButton we will destroy the whole activity with no saved data(last joke)
        //but when he press the back(the back besides home button) the last joke will be saved
        //and when the user gets back to the joke activity it will appear to him/her

    }

    public void generateAjoke() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.api-ninjas.com/v1/jokes?limit=";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Item = response.getJSONObject(0);
                            String joke1 = Item.getString("joke");
                            joke.setText(joke1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("VolleyExample", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", X_API_KEY);
                return headers;
            }
        };
        queue.add(jsonArrayRequest);


    }

    public void btnBackOnClick(View view) {
        joke.setText("");
        editor.remove(key);
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String str = joke.getText().toString();
        saveInSharedPrefrence(joke.getText().toString());
    }

    private void saveInSharedPrefrence(String quote) {
        if (editor != null) {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(quote);
            if (jsonStr != null) {
                editor.putString(key, jsonStr);
                editor.commit();
            }
        }
    }

    private String loadIN() {
        String jsonStr = prefs.getString(key, "");
        if (jsonStr != null) {
            Gson gson = new Gson();
            String quote = gson.fromJson(jsonStr, String.class);
            if (quote != null) {
                return quote;
            }
            return null;
        }
        return null;
    }

    public void btnJokeOnClick(View view) {
        generateAjoke();
    }
}


