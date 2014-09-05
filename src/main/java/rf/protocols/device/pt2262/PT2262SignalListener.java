package rf.protocols.device.pt2262;

import rf.protocols.core.Interval;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PT2262SignalListener implements SignalLengthListener {

    private static enum State {Separator, SecondSeparator, Signal, Short, Long}

    private PacketListener<BitPacket> packetListener;
    private PT2262SignalListenerProperties properties = new PT2262SignalListenerProperties();

    private BitPacket packet = new BitPacket(100);
    private State waitingFor = State.Separator;
    private Interval secondSeparatorInterval = new Interval(-1);
    private Interval shortInterval = new Interval(-1);
    private Interval longInterval = new Interval(-1);

    public PT2262SignalListener(PacketListener<BitPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public PT2262SignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(PT2262SignalListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        boolean reset = false;

        if (waitingFor != State.Separator && waitingFor != State.SecondSeparator &&
                isSecondSeparatorSignal(lengthInMicros)) {

            if (packet.getSize() >= properties.minPacketSize)
                packetListener.onPacket(packet);

            packet.clear();
            waitingFor = State.Separator;
            return;
        }

        switch (waitingFor) {
            case Separator:
                if (isSeparatorSignal(lengthInMicros)) {
                    waitingFor = State.Signal;
                    setSeparatorLength(lengthInMicros);
                }
                return; // !!! not break

            // doesn't happen after bad RF data quality
//            case SecondSeparator:
//                if (isSecondSeparatorSignal(lengthInMicros)) {
//                    waitingFor = State.Signal;
//                    return;  // !!! not break
//                }
//                break;

            case Signal:
                if (isShortSignal(lengthInMicros)) {
                    waitingFor = State.Long;
                } else if (isLongSignal(lengthInMicros)) {
                    waitingFor = State.Short;
                } else {
                    reset = true;
                }
                break;

            case Short:
                if (isShortSignal(lengthInMicros)) {
                    packet.addBit(false);
                    waitingFor = State.Signal;
                } else {
                    reset = true;
                }
                break;

            case Long:
                if (isLongSignal(lengthInMicros)) {
                    packet.addBit(true);
                    waitingFor = State.Signal;
                } else {
                    reset = true;
                }
                break;
        }

        if (reset) {
            packet.clear();

            if (isSeparatorSignal(lengthInMicros)) {
                waitingFor = State.Signal;
                setSeparatorLength(lengthInMicros);
            } else {
                waitingFor = State.Separator;
            }
        }
    }

    private boolean isSeparatorSignal(long l) {
        return l >= properties.minSeparatorLength && l <= properties.maxSeparatorLength;
    }

    private void setSeparatorLength(long l) {
        secondSeparatorInterval.setMed(l);
        secondSeparatorInterval.setDelta(properties.secondSeparatorDelta);

        shortInterval.setMed((long) (l * properties.shortSignalFraction));
        shortInterval.setTolerance(properties.shortSignalTolerance);

        longInterval.setMed((long) (l * properties.longSignalFraction));
        longInterval.setTolerance(properties.longSignalTolerance);
    }

    private boolean isSecondSeparatorSignal(long l) {
        return secondSeparatorInterval.isInside(l);
    }

    private boolean isShortSignal(long lengthInMicros) {
        return shortInterval.isInside(lengthInMicros);
    }

    private boolean isLongSignal(long lengthInMicros) {
        return longInterval.isInside(lengthInMicros);
    }
}
