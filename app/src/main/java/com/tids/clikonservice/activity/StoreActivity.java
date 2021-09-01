package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tids.clikonservice.R;

public class StoreActivity extends AppCompatActivity {
    ImageView ivBack, qrPrinter;
    CardView edtRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ivBack=findViewById(R.id.back_btn);
        qrPrinter=findViewById(R.id.btn_print_qr_code);
        edtRegistration=findViewById(R.id.edt_registration);

        ivBack.setOnClickListener(v -> onBackPressed());

        edtRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StoreActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(StoreActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.row_edit_product_details_dialog_box, null);
                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        qrPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StoreActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(StoreActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.row_qrcode_generate_dialog_box, null);
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