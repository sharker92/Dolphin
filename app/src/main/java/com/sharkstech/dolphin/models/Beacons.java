package com.sharkstech.dolphin.models;

import com.kontakt.sdk.android.common.model.Model;
import static com.kontakt.sdk.android.common.model.Model.BEACON_PRO;

/**
 * Created by shark on 08/08/2017.
 */

public class Beacons  {
    private String mac;
    private String address;
    private String name;
    private String uniqueId;
    private String firmware;
    private Model model;
    private int batteryLevel;
    private int txPower;
    private int rssi;
    private String namespace;
    private String instanceId;
    private String UUID;
    private int major;
    private int minor;
    private String url;
    private boolean shuffled;
    private double distancia;
    private double distancia2;
    private double distancia3;

    public Beacons() {

        mac = "AA:AA:AA:AA:AA:AA";
        address ="BB:BB:BB:BB:BB:BB";
        name = "DEFAULT";
        uniqueId = "DFLT";
        firmware = "0.0";
        model = BEACON_PRO;
        batteryLevel = 0;
        txPower = 0;
        rssi = 0;
        namespace = "8831a86e2893171d8a23";
        instanceId = "111111111111";
        UUID = "8831a86e-4fa2-4e98-8024-2893171d8a23";
        major = 16811;
        minor = 1111;
        url = "";
        shuffled = false;
        distancia = 0;
        distancia2 = 0;
        distancia3 = 0;

    }
/*perfil seguro*/
    public Beacons(String mac, String name, String uniqueId, String firmware,
                   Model model, int batteryLevel, int txPower, int rssi, Boolean shuffled) {


        this.mac = mac;
        this.name = name;
        this.uniqueId = uniqueId;
        this.firmware = firmware;
        this.model = model;
        this.batteryLevel = batteryLevel;
        this.txPower = txPower;
        this.rssi = rssi;
        this.shuffled = shuffled;


    }
    /*eddystone*/
    public Beacons(String address,String namespace, String instanceId, String url, int txPower, int rssi,
                   Boolean shuffled) {


        this.address = address;
        this.namespace = namespace;
        this.instanceId = instanceId;
        this.url = url;
        this.txPower = txPower;
        this.rssi = rssi;
        this.shuffled = shuffled;


    }
    /*iBeacon*/
    public Beacons(String address, String UUID, int major, int minor, int txPower, int rssi,
                   Boolean shuffled) {


        this.address = address;
        this.UUID = UUID;
        this.major = major;
        this.minor = minor;
        this.txPower = txPower;
        this.rssi = rssi;
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
    public String getUUID() { return UUID; }
    public void setUUID(String uuid) { this.UUID = uuid; }
    public int getMajor() { return major; }
    public void setMajor(int major) { this.major = major; }
    public int getMinor() { return minor; }
    public void setMinor(int minor) { this.minor = minor; }
    public Boolean getShuffled() { return shuffled; }
    public void setShuffled(Boolean shuffled ) { this.shuffled = shuffled;}

    public double getDistancia() { return distancia;}
    public void setDistancia (double distancia ) { this.distancia = distancia;}

    public double getDistancia2() { return distancia2;}
    public void setDistancia2 (double distancia2 ) { this.distancia2 = distancia2;}

    public double getDistancia3() { return distancia3;}
    public void setDistancia3 (double distancia3 ) { this.distancia3 = distancia3;}


}
