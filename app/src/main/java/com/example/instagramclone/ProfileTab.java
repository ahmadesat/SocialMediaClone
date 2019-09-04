package com.example.instagramclone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText editProfileName,editProfileBio;
    private RadioButton editProfileGender;
    private RadioGroup editGenderList;
    private Button saveInfoBtn, logOutBtn;


    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(view);
    }

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);


        logOutBtn = view.findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                Intent intent = new Intent(getContext(), LogIn.class);
                startActivity(intent);
            }
        });


        editProfileName = view.findViewById(R.id.editProfileName);
        editProfileBio = view.findViewById(R.id.editProfileBio);
        editGenderList = view.findViewById(R.id.editGenderList);


        final ParseUser user = ParseUser.getCurrentUser();

        //Set the already saved information about the user
        if (user.get("Name") != null){
            editProfileName.setText(user.get("Name") + "");
        }else{
            editProfileName.setText("");
        }
        if (user.get("Bio") != null){
            editProfileBio.setText(user.get("Bio") + "");
        }else{
            editProfileBio.setText("");
        }


        saveInfoBtn = view.findViewById(R.id.saveInfo);
        saveInfoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                int genderID = editGenderList.getCheckedRadioButtonId();
                editProfileGender = view.findViewById(genderID);

                user.put("Name", editProfileName.getText().toString());
                user.put("Bio", editProfileBio.getText().toString());
                user.put("Gender", editProfileGender.getText().toString());

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        try{
                            if (e==null){
                                Toast.makeText(getContext(), "Information Saved Successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Error Occured, Please Try Again Later.", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception errorHappened){
                            Toast.makeText(getContext(), "Error Occured, Please Try Again Later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        return view;
    }

//    public void logOutUser(View view){
//        ParseUser.getCurrentUser().logOut();
//        Intent intent = new Intent(getContext(), LogIn.class);
//        startActivity(intent);
//    }


}
