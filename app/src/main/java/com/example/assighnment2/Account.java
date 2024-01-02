package com.example.assighnment2;

public class Account {
    String Name;
    String Password;
    String Email;
    String Gender;

    public Account() {
    }

    public Account(String name, String password, String email, String gender) {
        Name = name;
        Password = password;
        Email = email;
        Gender = gender;
    }

    @Override
    public String toString() {
        return "Account{" +
                "Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", Email='" + Email + '\'' +
                ", Gender='" + Gender + '\'' +
                '}';
    }
}
