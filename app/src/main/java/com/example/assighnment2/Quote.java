package com.example.assighnment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Quote extends AppCompatActivity {
    final static String X_API_KEY = "fUR1UVPNVZQVfdZtVh9Zkb5FtRBb8JwhwlON68lz";
    private static final String key = "quote";
    private ListView listView;
    private TextView finalQuote;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        SetComponents();
        //data set on the listView by strings.xml
        if(editor!=null){
            if(loadIN()!=null&&!loadIN().isEmpty()){
                finalQuote.setText(loadIN());
            }
        }
        //when user press the backButton we will destroy the whole activity with no saved data(last quote)
        //but when he press the back(the back besides home button) the last quote will be saved
        //and when the user gets back to the quote activity it will appear to him/her


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://api.api-ninjas.com/v1/quotes?category="+ parent.getItemAtPosition(position);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONObject firstItem = response.getJSONObject(0);
                                    String quote = firstItem.getString("quote");
                                    String author = firstItem.getString("author");
                                    String category = firstItem.getString("category");
                                    finalQuote.setText("{ "+quote+" }\n"+"author : "+author+"\n Category : "+category);
                                    saveInSharedPrefrence(finalQuote.getText().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley Error", "Error: " + error.getMessage());
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
        };
        listView.setOnItemClickListener(itemClickListener);
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
            String quote = gson.fromJson(jsonStr,String.class);
            if (quote != null) {
                return quote;
            }
            return null;
        }
        return null;
    }

    private void SetComponents() {
        listView=findViewById(R.id.listView);
        finalQuote=findViewById(R.id.QuoteFinal);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }
    public void btnBackOnClick(View view){
        finalQuote.setText("");
        editor.remove(key);
        Intent intent =new Intent(this,Choose.class);
        startActivity(intent);
    }
    @Override
    protected void onStop() {
        super.onStop();
        String str=finalQuote.getText().toString();
        saveInSharedPrefrence(finalQuote.getText().toString());
    }

}