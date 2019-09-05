package com.example.socialmediaapp;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePostTab extends Fragment implements View.OnClickListener{


    private ImageView imageToShare;
    private EditText imageCaption;
    private Button shareButton;
    private Bitmap imageBitmap;


    public SharePostTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_post_tab, container, false);


        imageToShare = view.findViewById(R.id.imageToShare);
        imageCaption = view.findViewById(R.id.imageCaption);
        shareButton = view.findViewById(R.id.shareButton);

        imageToShare.setOnClickListener(SharePostTab.this);
        shareButton.setOnClickListener(SharePostTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.imageToShare:
                //Getting permission to read the external storage from user
                if (android.os.Build.VERSION.SDK_INT >= 19 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else{
                    getChosenImage();
                }
                
                break;

            case R.id.shareButton:

                if (imageBitmap != null){

                    if (imageCaption.getText().toString().equals("")){
                        Toast.makeText(getContext(), "You Must Add A Caption", Toast.LENGTH_SHORT).show();

                    }else{

                        //Convert the image to an array of bytes (easier to compress and upload it)
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("pic.png",bytes);
                        ParseObject parseObject = new ParseObject("Images");
                        parseObject.put("Image", parseFile);
                        parseObject.put("Image_Caption", imageCaption.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Posting...");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    Toast.makeText(getContext(),"Image Has Been Published !", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(),"Error , please try again later.", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }

                } else{
                    Toast.makeText(getContext(), "You Must Choose A Picture", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    private void getChosenImage() {

        Toast.makeText(getContext(), "Choose A Picture", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000){

            if (resultCode == Activity.RESULT_OK) {

                //Do Something with your chosen image
                try{
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    imageBitmap = BitmapFactory.decodeFile(picturePath);
                    imageToShare.setImageBitmap(imageBitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
