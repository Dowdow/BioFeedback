package belt_connector;

import java.util.Observable;

public class FakeThread extends Observable implements Runnable {

    private boolean start = true;
    private int hrv = 50;

    @Override
    public void run() {
        while (start) {
            generateData();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("La pause du FakeThread n'a pas pue être effectuée");
            }
        }
    }

    // Generate fake data
    private void generateData() {
        setChanged();
        notifyObservers(new Integer(hrv));
        hrv++;
    }

    public void stop() {
        start = false;
    }
}
