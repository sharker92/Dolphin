package com.sharkstech.dolphin.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sharkstech.dolphin.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        /*Incluir flecha atras*/
        setupToolbar();
    }
    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //   actionBar.setIcon(R.mipmap.ic_sharksicon);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //   actionBar.setDisplayShowHomeEnabled(true);

        }
    }
}
