package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import belt_connector.BeltConnector;
import belt_connector.BeltConnectorZephyr;


public class BiofeedbackActivity extends Activity {

    private BeltConnector beltConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);


        if(MainActivity.macAddress == null) {
            Toast.makeText(this, "Vous devez selectionner un appareil avant de faire le BioFeedback", Toast.LENGTH_LONG).show();
        } else {
            beltConnector = new BeltConnectorZephyr(MainActivity.macAddress);
            beltConnector.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(beltConnector != null) {
            try {
                beltConnector.stop();
            } catch (Exception e) {
            }
        }
    }
}
