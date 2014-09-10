package rf.protocols.device.generic.intervals;

import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListener implements SignalLengthListener {

    private PacketListener<IntervalsPacket> packetListener;
    private IntervalsSignalListenerProperties properties = new IntervalsSignalListenerProperties();
    private IntervalsPacket packet = new IntervalsPacket();

    private boolean started = false;

    public IntervalsSignalListener(PacketListener<IntervalsPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public IntervalsSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(IntervalsSignalListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        if (!started) {
            if (properties.isSeparator(lengthInMicros, high)) {
                started = true;
            }
        } else {
            String intervalName = properties.getIntervalName(lengthInMicros);
            if (intervalName != null) {
                packet.addInterval(intervalName);
            } else {
                if (properties.isSeparator(lengthInMicros, high) && properties.isCorrectSize(packet.getSize())){
                    packetListener.onPacket(packet);
                }
                packet.clear();
                started = false;
            }
        }
    }
}
