package com.sharkstech.dolphin.models;

/**
 * Created by shark on 08/08/2017.
 */

public class Beacons {
    private String mac;
    private String name;
    private String uniqueId;
    private String firmware;
    private String model;
    private int batteryLevel;
    private int txPower;
    private int rssi;
    private String namespace;
    private String instanceId;
    private boolean shuffled;

  /*  public Beacons() {
    }*/

    public Beacons(String mac, String name, String uniqueId, String firmware,
                   String model, int batteryLevel, int txPower, int rssi,
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
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }



}
