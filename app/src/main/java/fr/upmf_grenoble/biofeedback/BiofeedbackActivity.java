package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import belt_connector.BeltConnectorFake;
import belt_connector.BeltConnectorZephyr;
import belt_connector.ZephyrSummaryPacket;

public class BiofeedbackActivity extends Activity implements Observer {

    private BeltConnectorZephyr beltConnectorZephyr;
    private BeltConnectorFake beltConnectorFake;
    private BiofeedbackActivityUpdater biofeedbackActivityUpdater;

    private TextView textZephyr;
    private TextView textFake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);

        textZephyr = (TextView) findViewById(R.id.text_zephyr);
        textFake = (TextView) findViewById(R.id.text_fake);

        biofeedbackActivityUpdater = new BiofeedbackActivityUpdater(textZephyr, textFake);

        beltConnectorFake = new BeltConnectorFake();
        beltConnectorFake.addObserver(this);
        beltConnectorFake.start();

        if(MainActivity.macAddress == null) {
            Toast.makeText(this, "Vous devez selectionner un appareil avant de faire le BioFeedback", Toast.LENGTH_LONG).show();
        } else {
            beltConnectorZephyr = new BeltConnectorZephyr(MainActivity.macAddress);
            beltConnectorZephyr.addObserver(this);
            beltConnectorZephyr.start();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof BeltConnectorFake) {
            biofeedbackActivityUpdater.setHrv((int) data);
        } else if(observable instanceof BeltConnectorZephyr) {
           biofeedbackActivityUpdater.setZephyrSummaryPacket((ZephyrSummaryPacket) data);
        }
        BiofeedbackActivity.this.runOnUiThread(biofeedbackActivityUpdater);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beltConnectorFake.stop();
        if(beltConnectorZephyr != null) {
            try {
                beltConnectorZephyr.stop();
            } catch (Exception e) {
            }
        }
    }

}
