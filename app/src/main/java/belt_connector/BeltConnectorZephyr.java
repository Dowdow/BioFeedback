package belt_connector;


import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

import zephyr.android.BioHarnessBT.BTClient;

public class BeltConnectorZephyr extends BeltConnector {

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
        zephyrConnectedListener = new ZephyrConnectedListener(new Handler(), null);
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


}
