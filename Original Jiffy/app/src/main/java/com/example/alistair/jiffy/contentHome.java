package com.example.alistair.jiffy;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.*;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by shere on 3/27/2018.
 */

public class contentHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);

        Button button1 = (Button) findViewById(R.id.button3);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PickMeUpPage = new Intent(contentHome.this, PickMeUp.class);
                startActivity(PickMeUpPage);


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DeliverItemPage = new Intent(contentHome.this, DeliverItem.class);
                startActivity(DeliverItemPage);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BuyAndDeliverPage = new Intent(contentHome.this, BuyAndDeliver.class);
                startActivity(BuyAndDeliverPage);
            }
        });
    }
}
