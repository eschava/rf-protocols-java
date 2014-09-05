package rf.protocols.device.oregon.v3;

import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV3SignalListener implements SignalLengthListener {

    private static final int MAX_PACKET_LENGTH = 100;

    private static enum State {Preambule, WaitingForShort, WaitingForLong}

    private final BitPacket packet = new BitPacket(MAX_PACKET_LENGTH);
    private final PacketListener<BitPacket> packetListener;
    private State state;
    private int preambuleSize;
    private boolean lastBit;

    private OregonV3SignalListenerProperties properties = new OregonV3SignalListenerProperties();

    public OregonV3SignalListener(PacketListener<BitPacket> packetListener) {
        this.packetListener = packetListener;
        reset();
    }

    public OregonV3SignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(OregonV3SignalListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        boolean reset = false;

        if (properties.signalLength.isInside(lengthInMicros)) {
            boolean isLong = lengthInMicros >= properties.minLongSignalLength;
            switch (state) {
                case Preambule:
                    if (!isLong) {
                        preambuleSize++;
                    } else if (preambuleSize >= properties.minPreambuleSize) {
                        addBit(false);
                        state = State.WaitingForLong;
                    } else {
                        reset = true;
                    }
                    break;

                case WaitingForShort:
                    if (isLong) {
                        reset = true;
                    } else {
                        addBit(false);
                        state = State.WaitingForLong;
                    }
                    break;

                case WaitingForLong:
                    if (isLong) {
                        addBit(true);
                    } else {
                        state = State.WaitingForShort;
                    }
                    break;
            }
        } else {
            reset = true;
        }

        if (reset) {
            reset();
        } else if (packet.getSize() == properties.packetSize) {
            packetListener.onPacket(packet);
            reset();
        }
    }

    private void addBit(boolean invert) {
        lastBit = lastBit ^ invert;
        packet.addBit(lastBit);
    }

    public void reset() {
        packet.clear();
        state = State.Preambule;
        preambuleSize = 0;
        lastBit = false;
    }
}
