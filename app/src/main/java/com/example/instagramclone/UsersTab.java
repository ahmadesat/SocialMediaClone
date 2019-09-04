package com.example.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {

    private ListView usersList;
    private ArrayList usersArray;
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

}
