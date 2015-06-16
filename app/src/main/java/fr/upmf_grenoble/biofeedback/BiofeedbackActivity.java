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

    private TextView textZephyr;
    private TextView textFake;

    private int hrv;
    private ZephyrSummaryPacket zephyrSummaryPacket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);

        textZephyr = (TextView) findViewById(R.id.text_zephyr);
        textFake = (TextView) findViewById(R.id.text_fake);

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
        System.out.println("Coucou activity " + data.toString());
        if(observable instanceof BeltConnectorFake) {
            hrv = (int) data;
            System.out.println("=======================");
            System.out.println("HRV : " + hrv);
            System.out.println("=======================");
        } else if(observable instanceof BeltConnectorZephyr) {
           zephyrSummaryPacket = (ZephyrSummaryPacket) data;
            System.out.println("=======================");
            System.out.println("Year : " + zephyrSummaryPacket.getTimestampYear());
            System.out.println("Heart Rate : " + zephyrSummaryPacket.getHeartRate());
            System.out.println("HRV : " + zephyrSummaryPacket.getHeartRateVariability());
            System.out.println("Battery Voltage : " + zephyrSummaryPacket.getBatteryVoltage());
            System.out.println("Battery level : " + zephyrSummaryPacket.getBatteryLevel() + "%");
            System.out.println("=======================");
        }
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
