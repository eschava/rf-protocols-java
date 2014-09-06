package rf.protocols.analysis.intervals;

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
            if (properties.isObservableInterval(lengthInMicros)) {
                started = true;
                packet.addLength(lengthInMicros);
            } else {
                packet.setBeforePacketLength(lengthInMicros);
            }
        } else {
            if (properties.isObservableInterval(lengthInMicros)) {
                packet.addLength(lengthInMicros);
            } else {
                if (packet.getLengths().size() >= properties.minSize) {
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
}
