package com.example.alistair.jiffy.Friday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alistair.jiffy.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BuyFill extends AppCompatActivity {
    private EditText mStoreField, mNoItemsField,mItemList, mDestinationField;

    private static final int AUTO_COMP_REQ_CODE = 2;

    private Button mPic, mConfirm;


    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userID;
    private String mStore;
    private String mNoOfItem;
    private String mList;
    private String mDestinat;
    private String mProfileImageUrl;

    private Uri resultUri;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private static final String TAG = "BuyFill";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/Arkhip_font.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build());

            setContentView(R.layout.activity_buy_fill);

            Toolbar toolbar = (Toolbar) findViewById(R.id.mtoolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


            mStoreField = (EditText) findViewById(R.id.store);
            mNoItemsField = (EditText) findViewById(R.id.NoOfItems);
            mItemList = (EditText) findViewById(R.id.Items);
            mDestinationField = (EditText) findViewById(R.id.Destination);
           // mProfileImage = (ImageView) findViewById(R.id.Destination);


            //mPic = (Button) findViewById(R.id.addPic);
            mConfirm = (Button) findViewById(R.id.proceed);

            mAuth = FirebaseAuth.getInstance();
            userID = mAuth.getCurrentUser().getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("FillOrderDetails");

            //getUserInfo();



            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUserInformation();
                    Toast.makeText(BuyFill.this, "Order Information Sent!", Toast.LENGTH_LONG).show();
                }
            });
            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("SA")
                    .build();

            autocompleteFragment.setFilter(typeFilter);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getName());
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }

            });
        }
        /*
        private void getUserInfo(){
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        if(map.get("store")!=null){
                            mStore = map.get("store").toString();
                            mStoreField.setText(mStore);
                        }
                        if(map.get("numItems")!=null){
                            mNoOfItem = map.get("numItems").toString();
                            mNoItemsField.setText(mNoOfItem);
                        }
                        if(map.get("list")!=null){
                            mList = map.get("list").toString();
                            mItemList.setText(mList);
                        }
                        if(map.get("destination")!=null){
                            mDestinat = map.get("destination").toString();
                            mDestinationField.setText(mDestinat);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        */



        private void saveUserInformation() {
            mStore = mStoreField.getText().toString();
            mNoOfItem = mNoItemsField.getText().toString();
            mList = mItemList.getText().toString();
            mDestinat = mDestinationField.getText().toString();
            //int selectId = mRadioGroup.getCheckedRadioButtonId();


            Map userInfo = new HashMap();
            userInfo.put("store", mStore);
            userInfo.put("numItems", mNoOfItem);
            userInfo.put("list", mList);
            userInfo.put("destination", mDestinat);
            mUserDatabase.updateChildren(userInfo);

            if(resultUri != null) {

                StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = filePath.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        finish();
                        return;
                    }
                });
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Map newImage = new HashMap();
                        newImage.put("profileImageUrl", downloadUrl.toString());
                        mUserDatabase.updateChildren(newImage);

                        finish();
                        return;
                    }
                });
            }else{
                finish();
            }

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == AUTO_COMP_REQ_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(BuyFill.this, data);
                Toast.makeText(BuyFill.this, "place "+place.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

        /*
       @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                final Uri imageUri = data.getData();
                resultUri = imageUri;
                mProfileImage.setImageURI(resultUri);
            }
        }
        */
    }