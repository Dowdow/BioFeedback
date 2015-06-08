package belt_connector;


import android.os.Handler;

import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.BTComms;
import zephyr.android.BioHarnessBT.ConnectListenerImpl;
import zephyr.android.BioHarnessBT.ConnectedEvent;
import zephyr.android.BioHarnessBT.ConnectedListener;
import zephyr.android.BioHarnessBT.PacketTypeRequest;
import zephyr.android.BioHarnessBT.ZephyrProtocol;

public class ZephyrConnectedListener extends ConnectListenerImpl implements ConnectedListener<BTClient> {

    private BTComms btComms;
    private ZephyrProtocol zephyrProtocol;
    private belt_connector.ZephyrPacketListener zephyrPacketListener;

    public ZephyrConnectedListener(Handler handler, byte[] dataBytes, BTComms btComms) {
        super(handler, dataBytes);
        this.btComms = btComms;

    }

    @Override
    public void Connected(ConnectedEvent<BTClient> eventArgs) {
        super.Connected(eventArgs);
        zephyrProtocol = new ZephyrProtocol(btComms, new PacketTypeRequest());
        zephyrPacketListener = new belt_connector.ZephyrPacketListener();
        zephyrProtocol.addZephyrPacketEventListener(zephyrPacketListener);

        zephyrProtocol.SetSummaryDataPacket(true);
        zephyrProtocol.SetEventPacket(true);

        System.out.println("Apparemment tu es connecté pélo !");
    }
}
