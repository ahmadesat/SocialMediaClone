package com.example.socialmediaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserProfile extends AppCompatActivity {

    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        linearLayout = findViewById(R.id.linearPosts);

        Intent receivedIntentObject = getIntent();
        //Getting the clicked username from the previous intent
        final String receivedUserName = receivedIntentObject.getStringExtra("username");
        setTitle(receivedUserName + "'s Posts");

        //Getting the posts uploaded by the given username
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Images");
        //Important condition to get the uploads from this username ONLY
        parseQuery.whereEqualTo("username", receivedUserName);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject post : objects){

                        //Getting the image caption and putting it in a Text View
                        final TextView imageCaption = new TextView(UserProfile.this);
                        String imageCaptionText = ("<b>" + receivedUserName + "</b>" + " : " + post.get("Image_Caption") + "");
                        imageCaption.setText(Html.fromHtml(imageCaptionText));

                        //Getting the image itself
                        ParseFile postImage = (ParseFile) post.get("Image");

                        postImage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null){

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                    //Setting the parameters of the imageView UI component inside the LinearLayout
                                    ImageView postImageView = new ImageView(UserProfile.this);
                                    LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);


                                    LinearLayout.LayoutParams imageCaption_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageCaption_params.setMargins(5,5,5,30);
                                    imageCaption.setLayoutParams(imageCaption_params);
                                    imageCaption.setGravity(Gravity.CENTER);
                                    imageCaption.setTextColor(Color.WHITE);
                                    imageCaption.setTextSize(14f);



                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(imageCaption);

                                }
                            }
                        });
                    }
                } else{
                    Toast.makeText(UserProfile.this,receivedUserName + " Has No Posts !",Toast.LENGTH_LONG).show();
                    finish();
                }

                dialog.dismiss();
            }
        });

    }
}
