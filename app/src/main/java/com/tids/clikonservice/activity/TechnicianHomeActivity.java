package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tids.clikonservice.R;

public class TechnicianHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}