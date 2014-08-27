package rf.protocols.debug.intervals;

import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListener implements SignalLengthListener {

    private PacketListener<IntervalsPacket> packetListener;
    private IntervalsSignalListenerProperties properties = new IntervalsSignalListenerProperties();
    private IntervalsPacket packet;

    private boolean started = false;

    public IntervalsSignalListener(PacketListener<IntervalsPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public synchronized void start() {
        packet = new IntervalsPacket();
    }

    @Override
    public synchronized void onSignal(boolean high, long lengthInMicros) {
        if (!started) {
            String intervalName = getIntervalName(lengthInMicros);
            if (intervalName != null) {
                started = true;
                packet.addInterval(intervalName);
            } else {
                packet.setBeforePacketLength(lengthInMicros);
            }
        } else {
            String intervalName = getIntervalName(lengthInMicros);
            if (intervalName != null) {
                packet.addInterval(intervalName);
            } else {
                if (packet.getIntervals().size() >= properties.minSize) {
                    packet.setAfterPacketLength(lengthInMicros);
                    packetListener.onPacket(packet);
                }
                packet.clear();
                started = false;
            }
        }
    }

    public IntervalsSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(IntervalsSignalListenerProperties properties) {
        this.properties = properties;
    }

    private String getIntervalName(long l) {
        if (l >= properties.interval0Min && l <= properties.interval0Max)
            return getIntervalName(properties.interval0Name, l);
        if (l >= properties.interval1Min && l <= properties.interval1Max)
            return getIntervalName(properties.interval1Name, l);
        if (l >= properties.interval2Min && l <= properties.interval2Max)
            return getIntervalName(properties.interval2Name, l);
        if (l >= properties.interval3Min && l <= properties.interval3Max)
            return getIntervalName(properties.interval3Name, l);
        if (l >= properties.interval4Min && l <= properties.interval4Max)
            return getIntervalName(properties.interval4Name, l);
        return null;
    }

    private String getIntervalName(String name, long length) {
        if (name.equals("%d"))
            return String.valueOf(length);
        return name;
    }
}
