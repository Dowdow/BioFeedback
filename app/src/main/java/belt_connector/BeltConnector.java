package belt_connector;

import java.util.Observable;
import java.util.Observer;

public abstract class BeltConnector extends Observable {

    public abstract void start();

    public abstract void stop();

}
