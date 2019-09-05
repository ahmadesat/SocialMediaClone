package com.example.socialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.parse.ParseUser;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class HomePage extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        //Set the adapter of the pager to the tabadapter that we made so it can read it
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabsLayout);
        tabLayout.setupWithViewPager(viewPager, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.aboutItem){

            final PrettyDialog prettyDialog = new PrettyDialog(HomePage.this);

            prettyDialog.setTitle("About This App")
                    .setMessage("This is my first time" + "\n"
                            + "experimenting with social media apps" + "\n"
                            + "Hope You Like It :)" )
                    .setIcon(R.drawable.info_icon)
                    .addButton(
                            "Okay",
                            R.color.pdlg_color_white,
                            R.color.colorPrimary,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    //What will the button do?
                                    prettyDialog.dismiss();
                                }
                            }
                    ).show();

        }

        else if (item.getItemId() == R.id.logoutUserItem){
            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(HomePage.this, LogIn.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
