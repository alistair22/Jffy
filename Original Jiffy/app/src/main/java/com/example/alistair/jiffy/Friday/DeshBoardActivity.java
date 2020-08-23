package com.example.alistair.jiffy.Friday;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alistair.jiffy.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class DeshBoardActivity extends AppCompatActivity {



    private static final String TAG = "DeshBoardActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deshboard);


        if(isServiceOK()){
            init();
        }
    }



    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeshBoardActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnh);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeshBoardActivity.this, House.class);
                startActivity(intent);
            }
        });
    }

   public boolean isServiceOK(){
       Log.d(TAG, "isServiceOK: Checking google services version");

       int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DeshBoardActivity.this);

       if(available == ConnectionResult.SUCCESS){
           Log.d(TAG, "isServiceOK: Google play services is working");
           return true;

       }
       else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
           Log.d(TAG, "isServiceOK: Error occured but we can fix it");
           Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DeshBoardActivity.this, available, ERROR_DIALOG_REQUEST);
           dialog.show();
       }else{
           Toast.makeText(this, "You can not make this Request", Toast.LENGTH_SHORT).show();
       }
       return false;

   }


}
