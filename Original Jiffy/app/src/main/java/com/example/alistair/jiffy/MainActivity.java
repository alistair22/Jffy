package com.example.alistair.jiffy;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignUp,btnSignIn;
    RelativeLayout rootLayout;
    ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference users;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
        }


        //Init_firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        //init the progress dialog
        progressDialog = new ProgressDialog(this);




        //initialise the buttons here
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
    }

    public void openSignUp(View view) {
        showRegisterDialog();

    }
    private void showRegisterDialog() {


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER ");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.register_layout,null);

        final MaterialEditText edtName = (MaterialEditText) register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = (MaterialEditText) register_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtEmail = (MaterialEditText) register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = (MaterialEditText) register_layout.findViewById(R.id.edtPassword);

        progressDialog.setMessage("Registering, Please wait...");


        dialog.setView(register_layout);



        //set register button on cardView
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();


                //validation of fields
                if (TextUtils.isEmpty(edtName.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter name", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtPhone.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter phone number", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter email address", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }


                if (TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter password", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if (edtPassword.getText().toString().length() > 10 ){
                    Snackbar.make(rootLayout,"Password must be less than 10 characters", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if (edtPassword.getText().toString().length() < 5 ){
                    Snackbar.make(rootLayout,"Password too short", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                progressDialog.show();
                //register new User
                mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //saver User to the online DB
                                User user = new User();
                                user.setName(edtName.getText().toString());
                                user.setPhone(edtPhone.getText().toString());
                                user.setEmail(edtEmail.getText().toString());
                                user.setPassword(edtPassword.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                progressDialog.hide();
                                                Snackbar.make(rootLayout,"Registration Successful", Snackbar.LENGTH_LONG)
                                                        .show();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.hide();
                                                Snackbar.make(rootLayout,"Failed "+e.getMessage(), Snackbar.LENGTH_LONG)
                                                        .show();
                                            }
                                        });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Snackbar.make(rootLayout,"Failed "+e.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });
        //set the close button to the cardView Layout
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        dialog.show();
    }
    public void openSignIn(View view) {
          showLoginDialog();
    }

    private void showLoginDialog() {

        progressDialog.setMessage("Signing In, Please wait...");



        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN ");
        dialog.setMessage("Please SignIn");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.login_layout,null);

        final MaterialEditText edtName = (MaterialEditText) login_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = (MaterialEditText) login_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtEmail = (MaterialEditText) login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = (MaterialEditText) login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);

        //set register button on cardView
        dialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();

                        //validation of fields
                        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (edtPassword.getText().toString().length() > 10) {
                            Snackbar.make(rootLayout, "Password must be less than 10 characters", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (edtPassword.getText().toString().length() < 5) {
                            Snackbar.make(rootLayout, "Password too short", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        //Login
                        progressDialog.show();
                        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        progressDialog.hide();
                                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.hide();
                                Snackbar.make(rootLayout, "Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

/*@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);

    }
*/

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
