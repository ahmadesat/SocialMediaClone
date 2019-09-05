package com.example.socialmediaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LogIn extends AppCompatActivity {

    private EditText username_login;
    private EditText password_login;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            openHomePage();
        }

        username_login = findViewById(R.id.username2);
        password_login = findViewById(R.id.password2);


        signUp = findViewById(R.id.signupText);
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void logIn(View view){

        //Let the user know that the system is signing him in
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In, Please Wait.");
        progressDialog.show();

        ParseUser.logInInBackground(username_login.getText().toString(),
                password_login.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null){
                            Toast.makeText(LogIn.this, "Account Login Successful", Toast.LENGTH_LONG).show();
                            openHomePage();
                        }else{
                            Toast.makeText(LogIn.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                });

    }

    //If the user clicks on anywhere on screen, it will hide the keyboard
    public void bgTap(View view){
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    0);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openHomePage() {
        Intent intent = new Intent(LogIn.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}