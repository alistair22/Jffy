package com.example.alistair.jiffy.Friday;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alistair.jiffy.R;

public class House extends AppCompatActivity {
    Button btnhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        btnhome = (Button) findViewById(R.id.home);
        init();


    }
    private void init() {

        btnhome.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
             // Intent intent = new Intent(House.this, House.class);
                //Intent intent = new I
                //startActivity(intent);
                //initty();
                AlertDialog.Builder builder = new AlertDialog.Builder(House.this);
                // builder.setCancelable(true);
                //  builder.setTitle("Title");
                builder.setMessage("Pick Your Poison")
                        .setPositiveButton("Convience Stores",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                            Intent BuyFillPage = new Intent(House.this, BuyFill.class);
                                            startActivity(BuyFillPage);

                                    }
                                })
                        .setNegativeButton("Food Outlets", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent BuyChoosePage = new Intent(House.this, BuyChoose.class);
                                startActivity(BuyChoosePage);

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


}
