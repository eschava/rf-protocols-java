package rf.protocols.device.remoteswitch;

import rf.protocols.core.Interval;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchSignalListener implements SignalLengthListener {

    private static enum State {Separator, Signal, Short, Long, Sync, SecondSeparator}

    private PacketListener<RemoteSwitchPacket> packetListener;
    private RemoteSwitchSignalListenerProperties properties = new RemoteSwitchSignalListenerProperties();

    // some kind of packet
    private long value = 0;
    private Boolean previousBit = null;
    private int size = 0;

    private State waitingFor = State.Separator;
    private Interval secondSeparatorInterval = new Interval(-1);
    private Interval shortInterval = new Interval(-1);
    private Interval longInterval = new Interval(-1);

    public RemoteSwitchSignalListener(PacketListener<RemoteSwitchPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public RemoteSwitchSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(RemoteSwitchSignalListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        boolean reset = false;

        switch (waitingFor) {
            case Separator:
                if (isSeparatorSignal(lengthInMicros)) {
                    waitingFor = State.Signal;
                    setSeparatorLength(lengthInMicros);
                }
                break;

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
                    waitingFor = State.Signal;
                    reset = !addBit(false);
                } else {
                    reset = true;
                }
                break;

            case Long:
                if (isLongSignal(lengthInMicros)) {
                    waitingFor = State.Signal;
                    reset = !addBit(true);
                } else {
                    reset = true;
                }
                break;

            case Sync:
                if (isShortSignal(lengthInMicros)) {
                    waitingFor = State.SecondSeparator;
                } else {
                    reset = true;
                }
                break;

            case SecondSeparator:
//                if (isSecondSeparatorSignal(lengthInMicros)) {
                if (lengthInMicros >= secondSeparatorInterval.getMin()) {
                    packetListener.onPacket(new RemoteSwitchPacket(size, value));
                }
                reset = true;
                break;
        }

        if (reset) {
            clear();

            if (isSeparatorSignal(lengthInMicros)) {
                waitingFor = State.Signal;
                setSeparatorLength(lengthInMicros);
            } else {
                waitingFor = State.Separator;
            }
        }
    }

    private boolean addBit(boolean bit) {
        if (previousBit == null) {
            previousBit = bit;
            return true;
        }

        if (bit) {
            // 01 is not possible
            if (!previousBit)
                return false;
            // true-true means 0
            value *= 3;
        } else {
            // both false-false and true-false are possible (means 1 and 2 respectively)
            value = 3 * value + (previousBit ? 2 : 1);
        }
        size++;
        previousBit = null;

        if (size == properties.packetSize) {
            waitingFor = State.Sync;
        }
        return true;
    }

    private void clear() {
        value = 0;
        size = 0;
        previousBit = null;
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
