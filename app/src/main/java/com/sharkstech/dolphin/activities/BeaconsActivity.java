package com.sharkstech.dolphin.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconDevice;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.SecureProfileListener;
import com.kontakt.sdk.android.ble.rssi.RssiCalculators;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.model.ResolvedId;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;
import com.kontakt.sdk.android.common.profile.ISecureProfile;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.sharkstech.dolphin.R;
import com.sharkstech.dolphin.models.Beacons;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.sharkstech.dolphin.adapters.BeaconAdapter;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kontakt.sdk.android.common.model.Model.BEACON_PRO;
import static com.sharkstech.dolphin.R.layout.activity_beacons;


public class BeaconsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = "ProximityManager";

    private ProximityManager proximityManager;


    //private TextView statusTextEddy;

    // ListView, Gridview y Adapters
    private ListView listView;
    private GridView gridView;
    private BeaconAdapter adapterListView;
    private BeaconAdapter adapterGridView;

    private ProgressBar progressBar;

    // Lista de nuestro modelo, beacons
    private List<Beacons> beacons;
    private Beacons[] beaconO = new Beacons[11];
    private Beacons[] beaconE = new Beacons[11];


    // Items en el option menu
    private MenuItem itemListView;
    private MenuItem itemGridView;
    private MenuItem itemScanBeacon;
    private MenuItem itemStopScan;

    // Variables
    private int counter = 0;
    private final int SWITCH_TO_LIST_VIEW = 0;
    private final int SWITCH_TO_GRID_VIEW = 1;
    private final int START_SCAN = 2;
    private final int STOP_SCAN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_beacons);
        //inicializacion estatica para proveer la clave API
        KontaktSDK.initialize(this);
        //Activar flecha de atras
        setupToolbar();
        //Declaraciones del Adaptador y otros

        this.beacons = getAllBeacons();

        /*inicializa beacons*/
        inicializaBeacons();
        /*inicializa list views, grid views y adapter*/
        setupOthers();

        //Initialize and configure proximity manager
        setupProximityManager();


    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //   actionBar.setIcon(R.mipmap.ic_sharksicon);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //   actionBar.setDisplayShowHomeEnabled(true);

        }
    }

    private void setupOthers() {

        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
    }

    private void setupProximityManager() {
        proximityManager = ProximityManagerFactory.create(this);


        //Configure proximity manager basic options
        proximityManager.configuration()
                //Using BALANCED for best performance/battery ratio
                .scanMode(ScanMode.BALANCED)
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                //OnDeviceUpdate callback will be received with 5 seconds interval
                .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(5))
                .rssiCalculator(RssiCalculators.DEFAULT);

        //Setting up Secure Profile listener
        proximityManager.setSecureProfileListener(createSecureProfileListener());
        proximityManager.setEddystoneListener(createEddystoneListener());


    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                //Check if proximity manager is already scanning
                if (proximityManager.isScanning()) {
                    Toast.makeText(BeaconsActivity.this, "Already scanning", Toast.LENGTH_SHORT).show();
                    return;
                }
                proximityManager.startScanning();
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(BeaconsActivity.this, "Scanning started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopScanning() {
        //Stop scanning if scanning is in progress
        if (proximityManager.isScanning()) {
            proximityManager.stopScanning();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
        }
    }

    //  Listener de perfil seguro
    private SecureProfileListener createSecureProfileListener() {
        return new SecureProfileListener() {
            /*lectura de beacons*/
            @Override
            public void onProfileDiscovered(ISecureProfile iSecureProfile) {
                Log.i(TAG, "onProfileDiscovered: " + iSecureProfile.toString());

/*sintaxis para recibir un beacon y verificar si no esta repetido*/
                if (beaconO != null) {
                    int a = 0;
                    int b = 0;
                   /*verifica si no se esta recibiendo la señal de un beacon ya recibido*/
                  /*impide leer beacons que no sean BEACON_ PRO*/
                    for (int i = 0; i < beaconO.length; ++i) {
                        if (beaconO[i].getUniqueId().equals(iSecureProfile.getUniqueId()) || !beaconO[i].getModel().equals(iSecureProfile.getModel())) {
                            ++a;
                            i = beaconO.length;
                        }
                    }
                   /*verifica si hay espacio en el array de objetos tipo BEACON*(hay espacio si estan inicialisados por el constructor)*/
                   /*verifica si el elemento recibido es un beacon del modelo deseado*/
                    if (a == 0) {
                        for (int i = 0; i < beaconO.length; ++i) {
                            if (beaconO[i].getUniqueId().equals("DFLT") && beaconO[i].getModel().equals(iSecureProfile.getModel())) {
                                b = i;
                                a = -1;
                                i = beaconO.length;
                            } else if (i == beaconO.length - 1 && a == 0) {
                                Log.i(TAG, "FALTA  ESPACIO PARA: " + iSecureProfile.toString());
                            }
                        }
                    }
                    if (a == -1) {
                        beaconO[b] = new Beacons(
                                iSecureProfile.getMacAddress(),
                                iSecureProfile.getName(),
                                iSecureProfile.getUniqueId(),
                                iSecureProfile.getFirmwareRevision(),
                                iSecureProfile.getModel(),
                                iSecureProfile.getBatteryLevel(),
                                convertTxPower(iSecureProfile.getTxPower()),
                                iSecureProfile.getRssi(),
                                iSecureProfile.getNamespace(),
                                iSecureProfile.getInstanceId(),
                                iSecureProfile.isShuffled());

                        /*Formula 1*/
                        beaconO[b].setDistancia(calculateAccuracy(beaconO[b].getTxPower(), beaconO[b].getRssi()));
                        /*Formula 2*/
                        beaconO[b].setDistancia2(getRange(beaconO[b].getTxPower(), beaconO[b].getRssi()));
                    /*agrega el elemento a la lista*/
                        addBeacon(beaconO[b]);
                    }
                }
            }

            /*Actualiza beacons*/
            @Override
            public void onProfilesUpdated(List<ISecureProfile> list) {
                /*actualiza los valores de los beacons leidos*/
                Log.i(TAG, "onProfilesUpdated: " + list.size());
/*ciclo para cada uno de los beacons leidos actualizados*/
                for (int i = 0; i < list.size(); ++i) {
                    for (int j = 0; j < beaconO.length; ++j) {
                        if (list.get(i).getUniqueId() == beaconO[j].getUniqueId()) {
                            beaconO[j].setRssi(list.get(i).getRssi());
                            /*corrimiento para la lista de beacons (la que desplagamos para visualizar)*/
                            for (int k = 0; k < beacons.size(); ++k) {
                                if (beacons.get(k).getUniqueId() == beaconO[j].getUniqueId()) {
                                    updateBeacon(k, beaconO[j]);
                                    k = beacons.size();
                                }
                            }
                            j = beaconO.length;
                        }
                    }
                }


            }

            /*elimina beacons que pierden conexión*/
            @Override
            public void onProfileLost(ISecureProfile iSecureProfile) {
                Log.e(TAG, "onProfileLost: " + iSecureProfile.toString());

/*elimina de la lista el beacon que perdio conexion*/
                for (int i = 0; i < beacons.size(); ++i) {
                    if (beacons.get(i).getUniqueId() == iSecureProfile.getUniqueId()) {
                        deleteBeacon(i);
                        i = beacons.size();
                    }

                }

/*elimina el beacon que perdion conexion del arreglo de objetos de los beacon*/
                for (int i = 0; i < beaconO.length; ++i) {
                    if (beaconO[i].getUniqueId() == iSecureProfile.getUniqueId()) {
                        beaconO[i] = new Beacons();
                        i = beaconO.length;
                    }

                }

            }
        };
    }

    //Listener de perfil Eddystone

    private EddystoneListener createEddystoneListener() {
        return new EddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i(TAG, "onEddystoneDiscovered: " + eddystone.toString());
                Log.i(TAG, "onEddystoneDiscovered2: " + namespace.toString());

            }

            @Override
            public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {
                Log.i(TAG, "onEddystonesUpdated: " + eddystones.size());
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.e(TAG, "onEddystoneLost: " + eddystone.toString());
            }
        };
    }


    // metodo para realizar acción al presionar un elemento del view
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.clickBeacon(beacons.get(position));
    }

    private void clickBeacon(Beacons beacon) {
        Toast.makeText(this, "Distancia: " + beacon.getDistancia(), Toast.LENGTH_SHORT).show();
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
        this.itemScanBeacon = menu.findItem(R.id.scan_beacon);
        this.itemStopScan = menu.findItem(R.id.stop_scan);

        return true;
    }

    //Manejamos eventos click en el menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Eventos para los clicks en los botones del options menu
        switch (item.getItemId()) {
            case R.id.add_beacon:
                this.addBeacon(new Beacons("AA:7E:AB:33:1C:30", "SHRKR" + (++counter), "MIO", "2.0", BEACON_PRO, 100, 0, -74, "", "", false));
                return true;
            case R.id.scan_beacon:
                this.scanStartStopBeacons(this.START_SCAN);
                return true;
            case R.id.stop_scan:
                this.scanStartStopBeacons(this.STOP_SCAN);
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

    private void scanStartStopBeacons(int option) {
        //Metodo para detener e iniciar escaneo de Beacons
        if (option == START_SCAN) {
            itemScanBeacon.setVisible(false);
            itemStopScan.setVisible(true);
            startScanning();
        } else if (option == STOP_SCAN) {
            itemScanBeacon.setVisible(true);
            itemStopScan.setVisible(false);
            stopScanning();
        }

    }

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


    // CRUD actions - Get, Add, Delete
    private List<Beacons> getAllBeacons() {
        List<Beacons> list = new ArrayList<Beacons>() {{
            //add(new Beacons("AA:7E:AB:33:1C:30", "SHK", "utbA", "2.0", "BEACON_PRO", 100, -12, -74, "", "", false));

        }};
        return list;
    }

    /*Agrega objeto Beacon a la lista*/
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


    private void updateBeacon(int l, Beacons beacon) {
        this.beacons.set(l, beacon);
        // Avisamos del cambio en ambos adapters
        this.adapterListView.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }

    private void inicializaBeacons() {
        for (int i = 0; i < beaconO.length; ++i) {
            beaconO[i] = new Beacons();
            beaconE[i] = new Beacons();
        }
    }


    //calcula distancia
    protected static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }

    }

    protected static double getRange(int txCalibratedPower, double rssi) {

        double ratio_db = txCalibratedPower - rssi;
        double ratio_linear = Math.pow(10, ratio_db / 10);
        double r = Math.sqrt(ratio_linear);
        return r;
    }

    //convierte el valor decimal de TX al valor real dBm
    static int convertTxPower(int tx) {
        int dBm = 0;
        switch (tx) {
            case 4:
                dBm = -59;
                break;
            case 0:
                dBm = -65;
                break;
            case -4:
                dBm = -69;
                break;
            case -8:
                dBm = -72;
                break;
            case -12:
                dBm = -77;
                break;
            case -16:
                dBm = -81;
                break;
            case -20:
                dBm = -84;
                break;
            case -30:
                dBm = -115;
                break;
            default:
                dBm = -1;
        }
        return dBm;
    }


}
