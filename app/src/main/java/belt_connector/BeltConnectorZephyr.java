package belt_connector;


import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

import java.util.Observable;
import java.util.Observer;

import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.ZephyrPacket;

public class BeltConnectorZephyr extends BeltConnector implements Observer {

    private BTClient btClient;
    private BluetoothAdapter bluetoothAdapter;
    private ZephyrConnectedListener zephyrConnectedListener;

    public BeltConnectorZephyr(String macAddress) {
        super();
        if(!macAddress.matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$")) {
            throw new IllegalArgumentException("L'adresse MAC n'est pas valide");
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new NullPointerException("L'appareil ne supporte pas le bluetooth");
        }
        btClient = new BTClient(bluetoothAdapter, macAddress);
        zephyrConnectedListener = new ZephyrConnectedListener(new Handler(), null, this);
        btClient.addConnectedEventListener(zephyrConnectedListener);
    }

    @Override
    public void start() {
        btClient.start();
    }

    @Override
    public void stop() {
        btClient.Close();
    }


    @Override
    public void update(Observable observable, Object data) {
        ZephyrSummaryPacket zephyrSummaryPacket = (ZephyrSummaryPacket) data;
        System.out.println("=======================");
        System.out.println("Year : " + zephyrSummaryPacket.getTimestampYear());
        System.out.println("Heart Rate : " + zephyrSummaryPacket.getHeartRate());
        System.out.println("HRV : " + zephyrSummaryPacket.getHeartRateVariability());
        System.out.println("Battery Voltage : " + zephyrSummaryPacket.getBatteryVoltage());
        System.out.println("Battery level : " + zephyrSummaryPacket.getBatteryLevel() + "%");
        System.out.println("=======================");
        notifyObservers(data);
    }
}
