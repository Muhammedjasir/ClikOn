package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tids.clikonservice.R;

public class ServiceStatusMainActivity extends AppCompatActivity {
    ImageView ivBack, ivQr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_status_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ivQr=findViewById(R.id.btn_qr);
        ivBack=findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        ivQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceStatusMainActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(ServiceStatusMainActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.row_view_qr, null);
                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}