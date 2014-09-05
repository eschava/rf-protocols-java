package rf.protocols.analysis.breakdown;

import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BreakdownSignalListener extends TimerTask implements SignalLengthListener {

    private PacketListener<BreakdownPacket> packetListener;
    private BreakdownSignalListenerProperties properties = new BreakdownSignalListenerProperties();
    private BreakdownPacket packet;

    public BreakdownSignalListener(PacketListener<BreakdownPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public synchronized void start() {
        packet = new BreakdownPacket(properties.groupCount);
        new Timer(true).schedule(this, properties.period, properties.period);
    }

    @Override
    public synchronized void onSignal(boolean high, long lengthInMicros) {
        if (lengthInMicros < properties.minLength) {
            packet.incrementLessThanGroupped();
        } else {
            int groupSize = (properties.maxLength - properties.minLength) / properties.groupCount;
            int group = (int) ((lengthInMicros - properties.minLength) / groupSize);
            if (group < properties.groupCount)
                packet.incrementGroup(group);
            else
                packet.incrementMoreThanGroupped();
        }
    }

    @Override
    public synchronized void run() {
        packetListener.onPacket(packet);
        packet.clear();
    }

    public BreakdownSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(BreakdownSignalListenerProperties properties) {
        this.properties = properties;
    }
}
