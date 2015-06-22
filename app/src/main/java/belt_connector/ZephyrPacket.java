package belt_connector;

public abstract class ZephyrPacket {

    public abstract void initialize(byte[] bytes);

    // Transforme 2 bits en int
    protected int twoBytesToInt(byte b1, byte b2) {
        return (int) ((b1 << 8) | (b2 & 0xFF));
    }

    // Transforme 2 bits en float
    protected float twoBytesToFloat(byte b1, byte b2) {
        return (float) ((b1 << 8) | (b2 & 0xFF));
    }
}
