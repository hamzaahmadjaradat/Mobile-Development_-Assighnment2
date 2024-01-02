package com.example.assighnment2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;

    private TextView Bugger;
    String genderSelected = "";
    private static final String DATA = "Register";
    private static final String FLAG = "finish";


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    boolean catcher2 = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SetCompenents();
        if (editor != null) {
            if (loadIN() != null) {
                boolean catcher = prefs.getBoolean(FLAG, false);
                if (catcher) {
                    Account x = loadIN();
                    Name.setText(x.Name);
                    Email.setText(x.Email);
                    Password.setText(x.Password);
                }
                editor.putBoolean(FLAG, true);
            }
        }
        selectedGender();
    }

    private void SetCompenents() {
        Name = findViewById(R.id.Name_entry);
        Email = findViewById(R.id.Email_entry);
        Password = findViewById(R.id.Password_entry);
        gender = findViewById(R.id.Group_Gender);
        male = findViewById(R.id.Male);
        female = findViewById(R.id.Female);
        Bugger = findViewById(R.id.BUGER);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public void selectedGender() {
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);

                if (radioButton != null) {
                    genderSelected = radioButton.getText().toString();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (catcher2) {
            String name = Name.getText().toString();
            String password = Password.getText().toString();
            String email = Email.getText().toString();
            String genderS = genderSelected;
            Account x = new Account(name, password, email, genderS);
            saveInSharedPrefrence(x);
        }

    }

    public void btnFinishOnClick(View view) {
        Intent resultIntent = new Intent();
        String name = Name.getText().toString();
        String password = Password.getText().toString();
        String email = Email.getText().toString();
        String genderS = genderSelected;
        if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty() && !genderS.isEmpty()) {
            List<Account> list = loadINAccounts();
            boolean checkDuplicate = true;
            for (Account i : list) {
                if (i.Email.equals(email.trim())) {
                    checkDuplicate = false;
                }
            }
            if (checkDuplicate) {
                String str = "" + name.trim() + " " + password.trim() + " " + email.trim() + " " + genderS.trim();
                resultIntent.putExtra("Accounts", str);
                setResult(Activity.RESULT_OK, resultIntent);
                editor.putBoolean(FLAG, false);
                editor.commit();
                catcher2 = false;
                finish();
            } else {
                Bugger.setText(" Email already has Account, use another Email");
            }
        } else {
            Bugger.setText("make sure to fill all required BOXES");
        }

    }

    private void saveInSharedPrefrence(Account account) {
        if (editor != null) {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(account);
            if (jsonStr != null) {
                editor.putString(DATA, jsonStr);
                editor.commit();
            }
        }
    }

    private List<Account> loadINAccounts() {
        String jsonStr = prefs.getString("Accounts", "");
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

    private Account loadIN() {
        String jsonStr = prefs.getString(DATA, "");
        if (jsonStr != null) {
            Gson gson = new Gson();
            Account account = gson.fromJson(jsonStr, Account.class);
            if (account != null) {
                return account;
            }
            return null;
        }
        return null;
    }
}