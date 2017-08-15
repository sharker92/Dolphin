package com.sharkstech.dolphin.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import android.widget.Toast;

import com.sharkstech.dolphin.R;
import com.sharkstech.dolphin.models.Beacons;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.sharkstech.dolphin.adapters.BeaconAdapter;


import java.util.ArrayList;
import java.util.List;

import static com.sharkstech.dolphin.R.layout.activity_beacons;


public class BeaconsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ProximityManager proximityManager;
    //private TextView statusTextEddy;

    // ListView, Gridview y Adapters
    private ListView listView;
    private GridView gridView;
    private BeaconAdapter adapterListView;
    private BeaconAdapter adapterGridView;

    // Lista de nuestro modelo, beacons
    private List<Beacons> beacons;

    // Items en el option menu
    private MenuItem itemListView;
    private MenuItem itemGridView;

    // Variables
    private int counter = 0;
    private final int SWITCH_TO_LIST_VIEW = 0;
    private final int SWITCH_TO_GRID_VIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_beacons);
        //Activar flecha de atras
        setupToolbar();
        //Inicialización dinamica de API
        // KontaktSDK.initialize("amwGefsIOPirddbsEWkXusNXUTIEdnAD");

        this.beacons = getAllBeacons();

        this.listView = (ListView) findViewById(R.id.listView);
        this.gridView = (GridView) findViewById(R.id.gridView);
        // Adjuntando el mismo método click para ambos
        this.listView.setOnItemClickListener(this);
        this.gridView.setOnItemClickListener(this);



        // Enlazamos con nuestro adaptador personalizado
        this.adapterListView = new BeaconAdapter(this, R.layout.activity_beacons_list, beacons);
        this.adapterGridView = new BeaconAdapter(this, R.layout.activity_beacons_grid, beacons);
        this.listView.setAdapter(adapterListView);
        this.gridView.setAdapter(adapterGridView);

        // Registrar el context menu para ambos
        registerForContextMenu(this.listView);
        registerForContextMenu(this.gridView);

        // statusTextEddy = (TextView) findViewById(R.id.textViewEddy);

        /*proximityManager = ProximityManagerFactory.create(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setEddystoneListener(createEddystoneListener());*/
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }*/

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //   actionBar.setIcon(R.mipmap.ic_sharksicon);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //   actionBar.setDisplayShowHomeEnabled(true);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.clickBeacon(beacons.get(position));
        //Beacons beacon = new Beacons(mac,name,);

    }
    private void clickBeacon (Beacons beacon){
        Toast.makeText(this, "Beacon: " + beacon.getName(), Toast.LENGTH_SHORT).show();
    }

    //Inflamos el layout del menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el option menu con nuestro layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        // Después de inflar, recogemos las referencias a los botones que nos interesan
        this.itemListView = menu.findItem(R.id.list_view);
        this.itemGridView = menu.findItem(R.id.grid_view);
        return true;
    }
    //Manejamos eventos click en el menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Eventos para los clicks en los botones del options menu
        switch (item.getItemId()) {
            case R.id.add_beacon:
                this.addBeacon(new Beacons("AA:7E:AB:33:1C:30","SHK" + (++counter),"utbA","2.0","BEACON_PRO",100,-12,-74,"","",false ));
                return true;
            case R.id.list_view:
                this.switchListGridView(this.SWITCH_TO_LIST_VIEW);
                return true;
            case R.id.grid_view:
                this.switchListGridView(this.SWITCH_TO_GRID_VIEW);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Inflamos el context menu con nuestro layout
        MenuInflater inflater = getMenuInflater();
        // Antes de inflar, le añadimos el header dependiendo del objeto que se pinche
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(this.beacons.get(info.position).getName());
        // Inflamos
        inflater.inflate(R.menu.context_menu_beacons, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Obtener info en el context menu del objeto que se pinche
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_beacon:
                this.deleteBeacon(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

////////////////////////////////
    /*
    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                Log.i("Sample", "IBeacon discovered: " + ibeacon.toString());
            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.toString());
              //  statusTextEddy.setText(String.format("Eddystone discovered:" + eddystone.toString()));
            }
        };

    }*/
private void switchListGridView(int option) {
    // Método para cambiar entre Grid/List view
    if (option == SWITCH_TO_LIST_VIEW) {
        // Si queremos cambiar a list view, y el list view está en modo invisible...
        if (this.listView.getVisibility() == View.INVISIBLE) {
            // ... escondemos el grid view, y enseñamos su botón en el menú de opciones
            this.gridView.setVisibility(View.INVISIBLE);
            this.itemGridView.setVisible(true);
            // no olvidamos enseñar el list view, y esconder su botón en el menú de opciones
            this.listView.setVisibility(View.VISIBLE);
            this.itemListView.setVisible(false);
        }
    } else if (option == SWITCH_TO_GRID_VIEW) {
        // Si queremos cambiar a grid view, y el grid view está en modo invisible...
        if (this.gridView.getVisibility() == View.INVISIBLE) {
            // ... escondemos el list view, y enseñamos su botón en el menú de opciones
            this.listView.setVisibility(View.INVISIBLE);
            this.itemListView.setVisible(true);
            // no olvidamos enseñar el grid view, y esconder su botón en el menú de opciones
            this.gridView.setVisibility(View.VISIBLE);
            this.itemGridView.setVisible(false);
        }
    }
}
    // CRUD actions - Get, Add, Delete

    private List<Beacons> getAllBeacons() {
        List<Beacons> list = new ArrayList<Beacons>() {{
            add(new Beacons("AA:7E:AB:33:1C:30","SHK","utbA","2.0","BEACON_PRO",100,-12,-74,"","",false));

        }};
        return list;
    }

    private void addBeacon(Beacons beacon) {
        this.beacons.add(beacon);
        // Avisamos del cambio en ambos adapters
        this.adapterListView.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }

    private void deleteBeacon(int position) {
        this.beacons.remove(position);
        // Avisamos del cambio en ambos adapters
        this.adapterListView.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }

}