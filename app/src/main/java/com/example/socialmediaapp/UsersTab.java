package com.example.socialmediaapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView usersList;
    private ArrayList<String> usersArray;
    private ArrayAdapter usersAdapter;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        usersList = view.findViewById(R.id.usersList);
        usersArray = new ArrayList();

        //this android.R.layout row is pre-defined by Android
        usersAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, usersArray);


        usersList.setOnItemClickListener(UsersTab.this);
        usersList.setOnItemLongClickListener(UsersTab.this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        //To hide the current logged in user from the Users list
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data, Please Wait.");
        progressDialog.show();

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if (objects.size() > 0){
                        for (ParseUser user : objects){
                            usersArray.add(user.getUsername());
                        }

                        usersList.setAdapter(usersAdapter);
                    }
                }progressDialog.dismiss();

            }
        });

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), UserProfile.class);
        intent.putExtra("username", usersArray.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", usersArray.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null){

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());

                    prettyDialog.setTitle(user.getUsername() + "'s Info")
                            .setMessage("Name : " + user.get("Name") + "\n"
                                    + "Gender : " + user.get("Gender") + "\n"
                                    + "Bio : " + user.get("Bio"))
                            .setIcon(R.drawable.person_icon)
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
            }
        });

        return false;
    }
}
