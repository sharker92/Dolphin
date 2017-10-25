package com.sharkstech.dolphin.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sharkstech.dolphin.R;
import android.widget.Toast;
import android.support.annotation.NonNull;
import com.kontakt.sdk.android.common.KontaktSDK;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Boton
    private Button btnBeacons;
    private Button btnMap;
    private static final int LOCATION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializacion estatica para proveer la clave API
        KontaktSDK.initialize(this);
        //Mostrar Icono de la aplicación
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_sharksicon);
        setupButtons();
    }

    private void setupButtons() {
        //Declarando botones y listeners
        btnBeacons = (Button) findViewById(R.id.buttonBeacons);
        btnMap = (Button) findViewById(R.id.buttonMap);

        btnBeacons.setOnClickListener(this);
        btnMap.setOnClickListener(this);
    }

    //cambio de actividades al presionar botones
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonBeacons:
                checkPermissions();
                break;
            case R.id.buttonMap:
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void checkPermissions() {
        //revisión de permisos de ubicación android API 23 en adelante
        // comprobar version actual de android que estamos corriendo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Comprobar si ha aceptado, no ha aceptado, o nunca se le ha preguntado
            if (CheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Ha aceptado
                //Acceder al beacon activity
                Intent intent = new Intent(MainActivity.this, BeaconsActivity.class);
                startActivity(intent);
            } else {
                // Ha denegado o es la primera vez que se le pregunta
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // No se le ha preguntado aún
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                } else {
                    // Ha denegado
                    Toast.makeText(MainActivity.this, "Please, enable the request permission", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }
        } else { //versiones anteriores de android M
            if (CheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                //si concedio el permiso
            } else {
                //no concedio el permiso
                /*DESHABILITAR BOTONES*/
                Toast.makeText(MainActivity.this, "You declined the location permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //permiso de ubicación
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Estamos en el caso del teléfono
        switch (requestCode) {
            case LOCATION_CODE:

                String permission = permissions[0];
                int result = grantResults[0];
                //verifica nuevamente si el permiso es el correcto
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Comprobar si ha sido aceptado o denegado la petición de permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        // Concedió su permiso
                        Intent intent = new Intent(MainActivity.this, BeaconsActivity.class);
                        startActivity(intent);
                    } else {
                        // No concendió su permiso
                        Toast.makeText(MainActivity.this, "You declined the access", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    //comprueba si tenemos el permiso
    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
