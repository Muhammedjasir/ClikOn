package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tids.clikonservice.R;

public class DriversHomeActivity extends AppCompatActivity {

    LinearLayout btnCart;
    CardView demoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnCart = findViewById(R.id.btn_cart);
        demoLink = findViewById(R.id.almadina_hypermarket);

        demoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriversHomeActivity.this,StoreActivity.class);
                startActivity(intent);
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriversHomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

    }
}