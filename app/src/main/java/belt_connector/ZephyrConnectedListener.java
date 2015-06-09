package belt_connector;


import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.ConnectListenerImpl;
import zephyr.android.BioHarnessBT.ConnectedEvent;
import zephyr.android.BioHarnessBT.ConnectedListener;
import zephyr.android.BioHarnessBT.PacketTypeRequest;
import zephyr.android.BioHarnessBT.ZephyrProtocol;

public class ZephyrConnectedListener extends ConnectListenerImpl implements ConnectedListener<BTClient> {

    private ZephyrProtocol zephyrProtocol;
    private belt_connector.ZephyrPacketListener zephyrPacketListener;

    public ZephyrConnectedListener(Handler handler, byte[] dataBytes) {
        super(handler, dataBytes);
    }

    @Override
    public void Connected(ConnectedEvent<BTClient> eventArgs) {
        super.Connected(eventArgs);
        zephyrProtocol = new ZephyrProtocol(eventArgs.getSource().getComms(), createPacketTypeRequest());
        zephyrPacketListener = new belt_connector.ZephyrPacketListener();
        zephyrProtocol.addZephyrPacketEventListener(zephyrPacketListener);
    }

    private PacketTypeRequest createPacketTypeRequest() {
        PacketTypeRequest packetTypeRequest = new PacketTypeRequest();
        packetTypeRequest.EnableSummary(true);
        return packetTypeRequest;
    }
}
