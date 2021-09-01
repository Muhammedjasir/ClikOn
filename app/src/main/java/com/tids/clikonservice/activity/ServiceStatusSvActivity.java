package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tids.clikonservice.R;

public class ServiceStatusSvActivity extends AppCompatActivity {
    ImageView ivBack;
    CardView demolist;
    LinearLayout btn_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_status_sv);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ivBack=findViewById(R.id.back_btn);
        demolist = findViewById(R.id.demoClick);
        btn_filter = findViewById(R.id.btn_filter);

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceStatusSvActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(ServiceStatusSvActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.row_filter_dialog, null);
                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ivBack.setOnClickListener(v -> onBackPressed());

        demolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceStatusSvActivity.this,ServiceStatusMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}