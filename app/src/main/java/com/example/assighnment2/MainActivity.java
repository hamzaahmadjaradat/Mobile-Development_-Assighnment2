package com.example.assighnment2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1;
    public static final String NAME = "Name";
    public static final String PASS = "PASS";
    public static final String Flag = "FLAG";
    private static final String DATA = "Accounts";
    private CheckBox checkbox;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private EditText Email;
    private EditText password;
    private Button GO;
    private TextView register;
    private TextView Bugger;
    private boolean flag = false;
    List<Account> MainList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpCompenants();
        checkBefore();
    }

    public void setUpCompenants() {
        Email = findViewById(R.id.Email);
        password = findViewById(R.id.pass);
        checkbox = findViewById(R.id.checkBox);
        GO = findViewById(R.id.GO);
        register = findViewById(R.id.Register);
        Bugger = findViewById(R.id.Bugger);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public void boxOnClick(View view) {
        if (checkbox.isChecked()) {
            String name = Email.getText().toString();
            String pass = password.getText().toString();
            if (!flag) {
                editor.putString(NAME, name);
                editor.putString(PASS, pass);
                editor.putBoolean(Flag, true);
                editor.commit();
            }

        } else {
            editor.remove(NAME);
            editor.remove(PASS);
            editor.remove(Flag);
            editor.commit();
        }

    }

    public void checkBefore() {
        flag = prefs.getBoolean(Flag, false);
        if (flag) {
            String email = prefs.getString(NAME, "");
            String Password = prefs.getString(PASS, "");
            Email.setText(email);
            password.setText(Password);
            checkbox.setChecked(true);
        }
    }

    private void saveInSharedPrefrence(List list) {
        if (editor != null) {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(list);
            if (jsonStr != null) {
                editor.putString(DATA, jsonStr);
                editor.commit();
            }
        }
    }

    public void btnGoOnClick(View view) {
        if (loadIN() != null) {
            List<Account> list = new ArrayList<>();
            list = loadIN();
            boolean flag = false;
            String str = "";
            for (Account x : list) {
                if (x.Email.equals(Email.getText().toString())) {
                    if (x.Password.equals(password.getText().toString())) {
                        Intent intent = new Intent(this, Choose.class);
                        startActivity(intent);
                    } else {
                        Bugger.setText("wrong password,add again");
                    }
                    flag = true;
                }
            }
            if (!flag) {
                Bugger.setText("you need to register !!!!!");
            }

        }
    }

    public void btnSetOnClick(View view) {

    }

    private List<Account> loadIN() {
        String jsonStr = prefs.getString(DATA, "");
        if (jsonStr != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Account>>() {
            }.getType();
            List<Account> list = gson.fromJson(jsonStr, type);
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("Accounts");
                String[] Splitter = result.split(" ");
                List<Account> list = new ArrayList<>();
                Log.e("jaradatJaradat", "");
                if (loadIN() != null) {
                    list = loadIN();
                }
                Account x = new Account(Splitter[0], Splitter[1], Splitter[2], Splitter[3]);
                list.add(x);
                saveInSharedPrefrence(list);
            }
        }
    }

    public void RegisterHyperLink(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
    }
}