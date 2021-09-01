package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tids.clikonservice.R;

public class DemoPagenationActivity extends AppCompatActivity {
Button Collection, Transport, Technician;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_pagenation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Collection=findViewById(R.id.btn_collection);
        Transport=findViewById(R.id.btn_Transport);
        Technician=findViewById(R.id.btn_Technician);

        Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoPagenationActivity.this,StaffHomeActivity.class);
                startActivity(intent);
            }
        });
        Transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoPagenationActivity.this,DriversHomeActivity.class);
                startActivity(intent);
            }
        });
        Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoPagenationActivity.this,TechnicianHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}