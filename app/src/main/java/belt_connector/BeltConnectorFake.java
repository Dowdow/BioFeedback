package belt_connector;

import java.util.Observable;
import java.util.Observer;

public class BeltConnectorFake extends BeltConnector implements Observer {

    private FakeThread fakeThread;
    private Thread thread;

    public BeltConnectorFake(int progression, int time) {
        fakeThread = new FakeThread(progression, time);
        fakeThread.addObserver(this);
        thread = new Thread(fakeThread);
    }

    @Override
    public void update(Observable observable, Object data) {
        setChanged();
        notifyObservers(data);
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void stop() {
        fakeThread.stop();
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("Le FakeThread n'a pas pu être stoppé");
        }
    }


}
