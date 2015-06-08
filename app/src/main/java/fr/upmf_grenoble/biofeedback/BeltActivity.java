package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class BeltActivity extends Activity {

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> arrayDevices;

    private Button buttonBluetooth;
    private Button buttonSearch;
    private ListView listDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belt);

        arrayDevices = new ArrayAdapter<String>(this, R.layout.activity_belt_item);

        // Instanciation de BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,"Votre appareil ne supporte pas le bluetooth",Toast.LENGTH_LONG).show();
        }

        // Mise en place du bouton de recherche
        buttonSearch = (Button) findViewById(R.id.button_belt_search);
        buttonSearch.setOnClickListener(clickListener);

        // Mise en place du bouton d'activation bluetooth
        buttonBluetooth = (Button) findViewById(R.id.button_belt_bluetooth);
        buttonBluetooth.setOnClickListener(clickListener);
        if(bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                buttonBluetooth.setText(R.string.belt_desactiver);
            } else {
                buttonBluetooth.setText(R.string.belt_activer);
                buttonSearch.setClickable(false);
            }
        } else {
            buttonBluetooth.setText(R.string.belt_incompatible);
            buttonBluetooth.setClickable(false);
            buttonSearch.setClickable(false);
        }

        // Mise en place de la liste de devices
        listDevices = (ListView) findViewById(R.id.list_belt);
        listDevices.setAdapter(arrayDevices);
        listDevices.setOnItemClickListener(onItemClickListener);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // Case button bluetooth
                case R.id.button_belt_bluetooth:
                    if(bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                        buttonBluetooth.setText(R.string.belt_activer);
                        buttonSearch.setClickable(false);
                    } else {
                        bluetoothAdapter.enable();
                        buttonBluetooth.setText(R.string.belt_desactiver);
                        buttonSearch.setClickable(true);
                    }
                    break;
                // Case button search
                case R.id.button_belt_search:
                    // Récupération des périphériques appairés
                    arrayDevices.clear();
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            arrayDevices.add(device.getName() + "\n" + device.getAddress());
                        }
                    }
                    if(bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    bluetoothAdapter.startDiscovery();
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView device = (TextView) findViewById(R.id.text_device);
            TextView selected = (TextView) view;
            String[] text = selected.getText().toString().split("\n");
            device.setText("Périphérique selectionné : " + text[0]);
        }
    };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    arrayDevices.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(broadcastReceiver);
    }
}
