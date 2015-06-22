package belt_connector;

import java.util.Observable;

import zephyr.android.BioHarnessBT.ZephyrPacketArgs;
import zephyr.android.BioHarnessBT.ZephyrPacketEvent;

public class ZephyrPacketListener extends Observable implements zephyr.android.BioHarnessBT.ZephyrPacketListener {

    public static final int RR_PACKET_ID = 0x24;
    public static final int SUMMARY_PACKET_ID = 0x2b;

    @Override
    public void ReceivedPacket(ZephyrPacketEvent zephyrPacketEvent) {
        ZephyrPacketArgs zephyrPacketArgs = zephyrPacketEvent.getPacket();

        System.out.println("MSG ID = " + zephyrPacketArgs.getMsgID());

        ZephyrPacket zephyrPacket = null;
        switch (zephyrPacketArgs.getMsgID()) {
            case RR_PACKET_ID:
                zephyrPacket = new ZephyrRRPacket();
                break;
            case SUMMARY_PACKET_ID:
                zephyrPacket = new ZephyrSummaryPacket();
                break;
        }
        if(zephyrPacket != null) {
            zephyrPacket.initialize(zephyrPacketArgs.getBytes());
            setChanged();
            notifyObservers(zephyrPacket);
        }
    }
}
