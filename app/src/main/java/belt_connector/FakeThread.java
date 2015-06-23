package belt_connector;

import java.util.Observable;
import java.util.Random;

public class FakeThread extends Observable implements Runnable {

    private boolean start = true;
    private float currentProgression;
    private float variation;
    private int currentTime;
    private boolean random = false;

    public FakeThread(int progression, int time) {
        this.currentTime = time;
        this.currentProgression = 50;
        this.variation = progression;
        this.variation /= 100;
        this.variation *= 50;
        this.variation /=time;
    }

    @Override
    public void run() {
        while (start) {
            generateData();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("La pause du FakeThread n'a pas pue être effectuée");
            }
        }
    }

    // Generate fake data
    private void generateData() {
        if(currentTime > 0) {
            if(random) {
                currentProgression += variation;
                currentProgression += variation;
                currentProgression += variation;
                random = false;
            } else {
                Random rand = new Random();
                int number = rand.nextInt(2);
                if(number == 0) {
                    currentProgression += variation;
                } else {
                    currentProgression -= variation;
                    random = true;
                }
            }
            setChanged();
            notifyObservers(currentProgression);
            currentTime--;
        } else {
            stop(false);
        }
    }

    public void stop(boolean destroy) {
        start = false;
        if(!destroy) {
            setChanged();
            notifyObservers("Done");
        }
    }
}
