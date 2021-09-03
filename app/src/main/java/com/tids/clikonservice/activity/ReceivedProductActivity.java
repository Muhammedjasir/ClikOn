package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;

public class ReceivedProductActivity extends AppCompatActivity {

    private ImageView ivBack,iv_scanner,iv_serial_num_add;
    private TextInputEditText edt_serial_num;
    private RecyclerView rv_scanned_products;
    private Button bt_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_product);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        iv_scanner = findViewById(R.id.iv_scanner);
        iv_serial_num_add = findViewById(R.id.iv_serial_num_add);
        edt_serial_num = findViewById(R.id.edt_serial_num);
        rv_scanned_products = findViewById(R.id.rv_scanned_products);
        bt_complete = findViewById(R.id.bt_complete);

        ivBack = findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),TechnicianHomeActivity.class);
        startActivity(intent);
    }
}