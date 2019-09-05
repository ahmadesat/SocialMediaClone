package com.example.socialmediaapp;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText editProfileName,editProfileBio;
    private RadioButton editProfileGender;
    private RadioGroup editGenderList;
    private Button saveInfoBtn;
    private LinearLayout linearLayout;


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


        editProfileName = view.findViewById(R.id.editProfileName);
        editProfileBio = view.findViewById(R.id.editProfileBio);
        editGenderList = view.findViewById(R.id.editGenderList);


        linearLayout = view.findViewById(R.id.linearPosts);

        //Getting the posts uploaded by the given username
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Images");
        //Important condition to get the uploads from this username ONLY
        parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (final ParseObject post : objects) {

                        //Adding a delete post button
                        final ImageButton imageDelete = new ImageButton(getContext());
                        imageDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                post.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null){
                                            Toast.makeText(getContext(),"Post Has Been Deleted",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getContext(),"Error, Please Try Again Later.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                        //Getting the image caption and putting it in a Text View
                        final TextView imageCaption = new TextView(getContext());
                        String imageCaptionText = ("<b>" + ParseUser.getCurrentUser().getUsername() + "</b>" + " : " + post.get("Image_Caption") + "");
                        imageCaption.setText(Html.fromHtml(imageCaptionText));

                        //Getting the image itself
                        ParseFile postImage = (ParseFile) post.get("Image");

                        postImage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    //Setting the parameters of the imageView UI component inside the LinearLayout
                                    ImageView postImageView = new ImageView(getContext());
                                    LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5, 5, 5, 5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);


                                    LinearLayout.LayoutParams imageCaption_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageCaption_params.setMargins(5, 5, 5, 5);
                                    imageCaption.setLayoutParams(imageCaption_params);
                                    imageCaption.setGravity(Gravity.CENTER);
                                    imageCaption.setTextColor(Color.WHITE);
                                    imageCaption.setTextSize(14f);

                                    LinearLayout.LayoutParams imageDelete_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageDelete_params.setMargins(5, 20, 5, 30);
                                    imageDelete.setLayoutParams(imageCaption_params);
                                    imageDelete.setBackgroundResource(R.drawable.delete_icon);


                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(imageCaption);
                                    linearLayout.addView(imageDelete);

                                }
                            }
                        });
                    }
                }

                dialog.dismiss();
            }
        });


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



}
