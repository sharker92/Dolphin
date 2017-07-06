package com.sharkstech.dolphin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kontakt.sdk.android.ble.manager.ProximityManager;

import com.kontakt.sdk.android.common.KontaktSDK;


public class MainActivity extends AppCompatActivity {

    private ProximityManager proximityManager;
    private Button btn;
    private String GREETER = "Que rollo pollo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mostrar Icono de la aplicación
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_sharksicon);
        //Boton Beacons Activity
        btn = (Button) findViewById(R.id.buttonBeacons);

        btn.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view){
               //Acceder al beacon activity y enviarle un string
               Intent intent = new Intent(MainActivity.this, Beacons.class);
               startActivity(intent);
           }
        });

    }
}
