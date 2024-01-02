package com.example.assighnment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Choose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }

    public void btnLogoutOnClick(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void btnQuoteOnClick(View view){
        Intent intent=new Intent(this,Quote.class);
        startActivity(intent);
    }
    public void btnJokeOnClick(View view){
        Intent intent=new Intent(this,Joke.class);
        startActivity(intent);
    }


}