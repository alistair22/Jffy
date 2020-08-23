package com.example.alistair.jiffy.Friday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.alistair.jiffy.R;

public class ActivityOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        TextView txtInfo = (TextView)findViewById(R.id.txtInfo);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            txtInfo.setText(info);
        }
    }
}
