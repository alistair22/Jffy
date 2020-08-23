package com.example.alistair.jiffy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alistair.jiffy.Friday.BuyChoose;
import com.example.alistair.jiffy.Friday.BuyFill;
import com.example.alistair.jiffy.Friday.DeshBoardActivity;
import com.example.alistair.jiffy.Friday.House;
import com.example.alistair.jiffy.Friday.MapActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private TextView mNameField, mEmailField, UserName, UserEmail;
    private ImageView mProfileImage,RiderImageView;

    private String mName;
    private String mEmail;
    private String userID;
    private String mProfileImageUrl;
    private Uri resultUri;

    FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    FirebaseUser user;


    private static final String TAG = "HomeActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_home);

        mProfileImage = (ImageView) findViewById(R.id.UserImageView);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        getUserInfo();

        Button button3 = (Button) findViewById(R.id.button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(isServiceOK()){
            init();
        }

    }
    private void init(){

        Button button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(House.this, House.class);
                //Intent intent = new I
                //startActivity(intent);
                //initty();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                // builder.setCancelable(true);
                //  builder.setTitle("Title");
                builder.setMessage("Pick Your Poison")
                        .setPositiveButton("Convience Stores",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent BuyFillPage = new Intent(HomeActivity.this, BuyFill.class);
                                        startActivity(BuyFillPage);

                                    }
                                })
                        .setNegativeButton("Food Outlets", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent BuyChoosePage = new Intent(HomeActivity.this, BuyChoose.class);
                                startActivity(BuyChoosePage);

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapActivityPage = new Intent(HomeActivity.this, MapActivity.class);
                startActivity(MapActivityPage);
            }
        });
    }

    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOK: Google play services is working");
            return true;

        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOK: Error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can not make this Request", Toast.LENGTH_SHORT).show();
        }
        return false;

    }


    private void getUserInfo() {
        userID = mAuth.getCurrentUser().getUid();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String profileImageUrl = dataSnapshot.child("profile").getValue(String.class);

                //initialise
                UserName = (TextView) findViewById(R.id.UserNameView);
                UserEmail = (TextView) findViewById(R.id.UserEmailView);
                RiderImageView = (ImageView) findViewById(R.id.profileImage);
                //set text to Nav drawer header
                UserName.setText(name);
                UserEmail.setText(email);


               // Glide.with(getApplication()).load(profileImageUrl).into(RiderImageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_foodOut) {
            Intent intent = new Intent(HomeActivity.this, BuyChoose.class);
            startActivity(intent);
        } else if (id == R.id.nav_conviStore) {
            Intent intent = new Intent(HomeActivity.this, BuyFill.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_shopHistory) {

        }else if (id == R.id.nav_pickMeUp) {
            Intent intent = new Intent(HomeActivity.this, MapActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_editProfile) {
            Intent intent = new Intent(HomeActivity.this, CustomerProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }
}