package rf.protocols.oregon.v2;

import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2SignalListener implements SignalLengthListener {

    private static final int MAX_PACKET_LENGTH = 100;

    private static enum State {Preambule, WaitingForShort, WaitingForLong}

    private final BitPacket packet = new BitPacket(MAX_PACKET_LENGTH);
    private final PacketListener packetListener;
    private State state;
    private int preambuleSize;
    private boolean lastBit;
    private boolean skipBit;

    private OregonV2SignalListenerProperties properties = new OregonV2SignalListenerProperties();

    public OregonV2SignalListener(PacketListener<BitPacket> packetListener) {
        this.packetListener = packetListener;
        reset();
    }

    public void setProperties(OregonV2SignalListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        boolean reset = false;

        if (lengthInMicros >= properties.minSignalLength && lengthInMicros <= properties.maxSignalLength) {
            boolean isLong = lengthInMicros >= properties.minLongSignalLength;
            switch (state) {
                case Preambule:
                    if (isLong)
                        preambuleSize++;
                    else if (preambuleSize >= properties.minPreambuleSize)
                        state = State.WaitingForShort;
                    else
                        reset = true;
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
        if (skipBit)
            skipBit = false;
        else
            packet.addBit(lastBit);
    }

    public void reset() {
        packet.clear();
        state = State.Preambule;
        preambuleSize = 0;
        lastBit = false;
        skipBit = false;
    }
}
