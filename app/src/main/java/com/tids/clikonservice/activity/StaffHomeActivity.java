package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tids.clikonservice.R;

public class StaffHomeActivity extends AppCompatActivity {
LinearLayout btnRegistration, btnServiceStatus, btnCustomerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnRegistration = findViewById(R.id.btn_registration);
        btnServiceStatus = findViewById(R.id.btn_service_status);
        btnCustomerService = findViewById(R.id.btn_customer_service);

        btnCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StaffHomeActivity.this,"Future Updates",Toast.LENGTH_SHORT).show();
                

            }
        });


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnServiceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this,ServiceStatusSvActivity.class);
                startActivity(intent);
            }
        });

    }
}