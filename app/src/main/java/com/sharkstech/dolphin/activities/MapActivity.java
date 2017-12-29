package com.sharkstech.dolphin.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

import com.sharkstech.dolphin.R;

import java.util.Random;

public class MapActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        /*Incluir flecha atras*/
        setupToolbar();



        final AppCompatImageView icAndroid = findViewById(R.id.ic_android);
        icAndroid.setImageResource(R.drawable.ic_android);
/*otro modo*/
        final ImageView icCasi = findViewById(R.id.ic_casi);
        icCasi.setImageResource(R.drawable.ic_casi);


        icAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rnd = new Random();
                double relacion, altoTotal, anchoTotal, altoReal, y = 0.0;
                float a = 0;
                float b = 0;
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                icAndroid.setColorFilter(color);

                // algoritmo para encontrar el punto 0,0 ajustable a cada pantalla
                altoTotal =icCasi.getHeight();
                anchoTotal =icCasi.getWidth();
                relacion = 52/21.5;
                altoReal = anchoTotal/relacion;
                y = (altoTotal - altoReal)/2;

                a = (float) 0;
                //b = (float) 64.76821302;
                b = (float) y;
                icAndroid.setX(a);
                icAndroid.setY(b);

                icCasi.getDrawable();


            }
        });

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
