package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUp extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        signIn = findViewById(R.id.signinText);
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void createAccount(View view) {
        try {
            ParseUser user = new ParseUser();
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setEmail(email.getText().toString());

            //Making an exception for when the user leaves one of the inputs empty
            if (username.getText().toString().equals("") ||
            password.getText().toString().equals("") ||
            email.getText().toString().equals("")) {

                Toast.makeText(SignUp.this, "Email,Username and Password are required !", Toast.LENGTH_SHORT).show();

            } else if(!email.getText().toString().contains("@") ||
                    !email.getText().toString().contains(".")) {
                Toast.makeText(SignUp.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();

            }
            else {

                //Let the user know that the account is being created
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Creating Account, Please Wait.");
                progressDialog.show();

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUp.this, LogIn.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Username/Email Already Exists", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }catch (Exception e) {

            Toast.makeText(SignUp.this, "Error Occured, Please Try Again Later.", Toast.LENGTH_LONG).show();

        }
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
}
