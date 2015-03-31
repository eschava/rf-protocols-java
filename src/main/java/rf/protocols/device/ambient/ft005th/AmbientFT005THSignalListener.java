package rf.protocols.device.ambient.ft005th;

import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientFT005THSignalListener implements SignalLengthListener {

    private static final int MAX_PACKET_LENGTH = 100;

    private final BitPacket packet = new BitPacket(MAX_PACKET_LENGTH);
    private final PacketListener<BitPacket> packetListener;

    private AmbientFT005THSignalListenerProperties properties = new AmbientFT005THSignalListenerProperties();

    public AmbientFT005THSignalListener(PacketListener<BitPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public void setProperties(AmbientFT005THSignalListenerProperties properties) {
        this.properties = properties;
    }

    public AmbientFT005THSignalListenerProperties getProperties() {
        return properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        if (high) {
            if (isOneSignal(lengthInMicros)) {
                packet.addBit(true);
            } else if (isZeroSignal(lengthInMicros)) {
                packet.addBit(false);
            } else {
                if (packet.getSize() >= properties.packetSize)
                    sendPacket();
                packet.clear();
            }
        } else {
            if (!isPauseSignal(lengthInMicros)) {
                if (packet.getSize() >= properties.packetSize)
                    sendPacket();
                packet.clear();
            }
        }
    }

    protected void sendPacket() {
        int startIndex = packet.indexOf(95, 9); // searching for start of message (001011111)
        if (startIndex >= 0 && startIndex + properties.packetSize <= packet.getSize()) {
            BitPacket packetToSend = packet.subpacket(startIndex + 1, properties.packetSize);
            packetListener.onPacket(packetToSend);
        }
    }

    private boolean isZeroSignal(long lengthInMsec) {
        return properties.zeroLength.isInside(lengthInMsec);
    }

    private boolean isOneSignal(long lengthInMsec) {
        return properties.oneLength.isInside(lengthInMsec);
    }

    private boolean isPauseSignal(long lengthInMsec) {
        return properties.pauseLength.isInside(lengthInMsec);
    }
}
