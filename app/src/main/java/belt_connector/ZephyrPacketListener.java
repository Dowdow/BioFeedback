package belt_connector;

import zephyr.android.BioHarnessBT.ZephyrPacketArgs;
import zephyr.android.BioHarnessBT.ZephyrPacketEvent;

public class ZephyrPacketListener implements zephyr.android.BioHarnessBT.ZephyrPacketListener {

    @Override
    public void ReceivedPacket(ZephyrPacketEvent zephyrPacketEvent) {
        ZephyrPacketArgs zephyrPacket = zephyrPacketEvent.getPacket();
        System.out.println("===== PACKET =====");
        System.out.println("Bytes : " + zephyrPacket.getBytes().toString());
        System.out.println("CRCS Status : " + zephyrPacket.getCRCStatus());
        System.out.println("Msg ID : " + zephyrPacket.getMsgID());
        System.out.println("Num Rvcd Bytes : " + zephyrPacket.getNumRvcdBytes());
        System.out.println("Status : " + zephyrPacket.getStatus());
        System.out.println("==================");
    }
}
