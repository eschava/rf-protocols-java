package rf.protocols.device.lacrosse;

import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.impl.BitPacket;

/**
 * http://www.f6fbb.org/domo/sensors/tx3_th.php
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseSignalListener implements SignalLengthListener {

    private static final int MAX_PACKET_LENGTH = 100;

    private final BitPacket packet = new BitPacket(MAX_PACKET_LENGTH);
    private final PacketListener<BitPacket> packetListener;

    private LacrosseSignalListenerProperties properties = new LacrosseSignalListenerProperties();

    public LacrosseSignalListener(PacketListener<BitPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public void setProperties(LacrosseSignalListenerProperties properties) {
        this.properties = properties;
    }

    public LacrosseSignalListenerProperties getProperties() {
        return properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        if (high) {
            if (packet.getSize() > 0) {
                if (!isPauseSignal(lengthInMicros))
                    packet.clear();
            }
        } else {
            if (isZeroSignal(lengthInMicros)) {
                packet.addBit(false);
            } else if (isOneSignal(lengthInMicros)) {
                packet.addBit(true);
            } else {
                packet.clear();
            }

            if (packet.getSize() == properties.packetSize) {
                packetListener.onPacket(packet);
                packet.clear();
            }
        }
    }

    private boolean isPauseSignal(long lengthInMsec) {
        return properties.pauseLength.isInside(lengthInMsec);
    }

    private boolean isZeroSignal(long lengthInMsec) {
        return properties.zeroLength.isInside(lengthInMsec);
    }

    private boolean isOneSignal(long lengthInMsec) {
        return properties.oneLength.isInside(lengthInMsec);
    }
}
