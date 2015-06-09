package belt_connector;

import zephyr.android.BioHarnessBT.ZephyrPacketArgs;
import zephyr.android.BioHarnessBT.ZephyrPacketEvent;

public class ZephyrPacketListener implements zephyr.android.BioHarnessBT.ZephyrPacketListener {

    public static final int SUMMARY_PACKET_ID = 0x2b;

    @Override
    public void ReceivedPacket(ZephyrPacketEvent zephyrPacketEvent) {
        ZephyrPacketArgs zephyrPacket = zephyrPacketEvent.getPacket();

        System.out.println("MSG ID = " + zephyrPacket.getMsgID());

        if(zephyrPacket.getMsgID() == SUMMARY_PACKET_ID) {
            ZephyrSummaryPacket zephyrSummaryPacket = new ZephyrSummaryPacket();
            zephyrSummaryPacket.initialize(zephyrPacket.getBytes());
        }
    }
}
