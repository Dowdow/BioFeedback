package fr.upmf_grenoble.biofeedback;

import java.util.LinkedList;

public class RRWindow {

    private static final int WINDOW_SIZE = 60;

    private LinkedList<Integer> window = new LinkedList<>();
    private LinkedList<Integer> currentWindow = new LinkedList<>();

    public boolean add(int rrInterval) {
        boolean full = false;
        if (window.size() > 0) {
            if (rrInterval != 0 && rrInterval != window.getLast()) {
                if (window.size() >= WINDOW_SIZE) {
                    window.removeFirst();
                    full = true;
                }
                window.addLast(rrInterval);
            }
        } else {
            window.addLast(rrInterval);
        }
        return full;
    }

    public double getSd1() {
        squareFilter();
        quotientFilter();
        return sd1();
    }

    private void squareFilter() {
        currentWindow.removeAll(currentWindow);
        for (int rr : window) {
            if (rr > 300 && rr < 2000) {
                currentWindow.addLast(rr);
            }
        }
    }

    private void quotientFilter() {
        LinkedList<Integer> tempWindow = new LinkedList<>();
        for (int i = 0; i < currentWindow.size() - 1; i++) {
            double x = currentWindow.get(i);
            double x1 = currentWindow.get(i + 1);
            double div1 =  x / x1;
            double div2 = x1 / x;
            if(div1 >= 0.8 && div1 <= 1.2 && div2 >= 0.8 && div2 <= 1.2) {
                tempWindow.addLast((int) x);
            }
        }
        currentWindow.removeAll(currentWindow);
        currentWindow.addAll(tempWindow);
    }

    private double sd1() {
        double sd1 = Math.pow(sdsd(), 2);
        sd1 *= 0.5;
        return Math.sqrt(sd1);
    }

    private double sdsd() {
        double sdsd = Math.sqrt(esperance1() - esperance2());
        return sdsd;
    }

    private double esperance1() {
        double val = 0;
        for (int rr : currentWindow) {
            val += Math.pow(rr, 2);
        }
        val /= currentWindow.size();
        return val;
    }

    private double esperance2() {
        double val = 0;
        for (int rr : currentWindow) {
            val += rr;
        }
        val = Math.pow(val / currentWindow.size(), 2);
        return val;
    }

}
