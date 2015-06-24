package fr.upmf_grenoble.biofeedback;

import java.util.LinkedList;

import belt_connector.ZephyrRRPacket;

public class RRWindow {

    private static final int WINDOW_SIZE = 70;

    private LinkedList<ZephyrRRPacket> window = new LinkedList<>();

    public boolean add(ZephyrRRPacket zephyrRRPacket) {
        boolean full = false;
        if(window.size() >= WINDOW_SIZE) {
            window.removeFirst();
            full = true;
        }
        window.addLast(zephyrRRPacket);
        return full;
    }

    public int getAvgRR() {
        return calulateAvgRR();
    }

    private int calulateAvgRR() {
        int avg = 0;
        for (ZephyrRRPacket zephyrRRPacket : window) {
            avg += zephyrRRPacket.getAvgRToRSample();
        }
        return avg / window.size();
    }

}
