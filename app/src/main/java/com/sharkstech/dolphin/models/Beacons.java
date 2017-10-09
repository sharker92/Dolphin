package com.sharkstech.dolphin.models;

import com.kontakt.sdk.android.common.model.Model;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;

import com.kontakt.sdk.android.ble.device.BeaconDevice;

import static com.kontakt.sdk.android.common.model.Model.BEACON_PRO;

/**
 * Created by shark on 08/08/2017.
 */

public class Beacons  {
    private String mac;
    private String name;
    private String uniqueId;
    private String firmware;
    private Model model;
    private int batteryLevel;
    private int txPower;
    private int rssi;
    private String namespace;
    private String instanceId;
    private boolean shuffled;
    private double distancia;
    private double distancia2;
    private double distancia3;

    public Beacons() {

        mac = "AA:AA:AA:AA:AA:AA";
        name = "DEFAULT";
        uniqueId = "DFLT";
        firmware = "0.0";
        model = BEACON_PRO;
        batteryLevel = 0;
        txPower = 0;
        rssi = 0;
        namespace = "";
        instanceId = "";
        shuffled = false;
        distancia = 0;
        distancia2 = 0;

    }

    public Beacons(String mac, String name, String uniqueId, String firmware,
                   Model model, int batteryLevel, int txPower, int rssi,
                   String namespace, String instanceId, Boolean shuffled) {


        this.mac = mac;
        this.name = name;
        this.uniqueId = uniqueId;
        this.firmware = firmware;
        this.model = model;
        this.batteryLevel = batteryLevel;
        this.txPower = txPower;
        this.rssi = rssi;
        this.namespace = namespace;
        this.instanceId = instanceId;
        this.shuffled = shuffled;


    }

    public String getMac() { return mac; }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    public String getFirmware() { return firmware; }
    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }
    public Model getModel() { return model; }
    public void setModel(Model model) {
        this.model = model;
    }
    public int getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    public int getTxPower() { return txPower; }
    public void setTxPower(int txPower ) {
        this.txPower = txPower;
    }
    public int getRssi() { return rssi; }
    public void setRssi(int rssi ) {
        this.rssi = rssi;
    }
    public String getNamespace() { return namespace; }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public Boolean getShuffled() { return shuffled; }
    public void setShuffled(Boolean shuffled ) { this.shuffled = shuffled;}

    public double getDistancia() { return distancia;}
    public void setDistancia (double distancia ) { this.distancia = distancia;}

    public double getDistancia2() { return distancia2;}
    public void setDistancia2 (double distancia2 ) { this.distancia2 = distancia2;}

    public double getDistancia3() { return distancia3;}
    public void setDistancia3 (double distancia3 ) { this.distancia3 = distancia3;}


}
