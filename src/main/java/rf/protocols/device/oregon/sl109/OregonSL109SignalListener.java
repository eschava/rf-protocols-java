package rf.protocols.device.oregon.sl109;

import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109SignalListener implements SignalLengthListener {

    private static final int MAX_PACKET_LENGTH = 100;

    private final BitPacket packet = new BitPacket(MAX_PACKET_LENGTH);
    private final PacketListener<BitPacket> packetListener;

    private boolean waitingForLongSignal = false;
    private OregonSL109SignalListenerProperties properties = new OregonSL109SignalListenerProperties();

    public OregonSL109SignalListener(PacketListener<BitPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public void setProperties(OregonSL109SignalListenerProperties properties) {
        this.properties = properties;
    }

    public OregonSL109SignalListenerProperties getProperties() {
        return properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        if (waitingForLongSignal) {
            if (isZeroSignal(lengthInMicros))
                packet.addBit(false);
            else if (isOneSignal(lengthInMicros))
                packet.addBit(true);
            else {
                if (isEndSignal(lengthInMicros) && packet.getSize() == properties.packetSize)
                    packetListener.onPacket(packet);
                packet.clear();
            }
            waitingForLongSignal = false;
        } else {
            if (isPreambuleSignal(lengthInMicros))
                waitingForLongSignal = true;
            else
                packet.clear();
        }
    }

    private boolean isPreambuleSignal(long lengthInMsec) {
        return properties.preambuleLength.isInside(lengthInMsec);
    }

    private boolean isZeroSignal(long lengthInMsec) {
        return properties.zeroLength.isInside(lengthInMsec);
    }

    private boolean isOneSignal(long lengthInMsec) {
        return properties.oneLength.isInside(lengthInMsec);
    }

    private boolean isEndSignal(long lengthInMsec) {
        return properties.endLength.isInside(lengthInMsec);
    }
}
