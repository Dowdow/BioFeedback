package fr.upmf_grenoble.biofeedback;

import android.widget.TextView;

import belt_connector.ZephyrSummaryPacket;

public class BiofeedbackActivityUpdater implements Runnable {

    private TextView textZephyr;
    private TextView textFake;

    private int hrv = 0;
    private ZephyrSummaryPacket zephyrSummaryPacket;

    public BiofeedbackActivityUpdater(TextView textZephyr, TextView textFake) {
        this.textZephyr = textZephyr;
        this.textFake = textFake;
    }

    @Override
    public void run() {
        if(zephyrSummaryPacket != null) {
            textZephyr.setText("HRV Zephyr : " + zephyrSummaryPacket.getHeartRate());
        }
        textFake.setText("HRV Fake : " + hrv);
    }

    public void setZephyrSummaryPacket(ZephyrSummaryPacket zephyrSummaryPacket) {
        this.zephyrSummaryPacket = zephyrSummaryPacket;
    }

    public void setHrv(int hrv) {
        this.hrv = hrv;
    }
}
