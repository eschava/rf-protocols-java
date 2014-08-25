package rf.protocols.debug.groups;

import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class DebugGroupsSignalListener extends TimerTask implements SignalLengthListener {

    private PacketListener<DebugGroupsPacket> packetListener;
    private DebugGroupsSignalListenerProperties properties = new DebugGroupsSignalListenerProperties();
    private volatile DebugGroupsPacket packet;

    public DebugGroupsSignalListener(PacketListener<DebugGroupsPacket> packetListener) {
        this.packetListener = packetListener;
    }

    public void start() {
        packet = new DebugGroupsPacket(properties.groupCount);
        new Timer(true).schedule(this, properties.period, properties.period);
    }

    @Override
    public synchronized void onSignal(boolean high, long lengthInMicros) {
        if (lengthInMicros < properties.minLength)
            packet.incrementLessThanGroupped();
        else if (lengthInMicros >= properties.maxLength)
            packet.incrementMoreThanGroupped();
        else {
            int groupSize = (properties.maxLength - properties.minLength) / properties.groupCount;
            packet.incrementGroup((int) ((lengthInMicros - properties.minLength) / groupSize));
        }
    }

    @Override
    public synchronized void run() {
        packetListener.onPacket(packet);
        packet.clear();
    }

    public DebugGroupsSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(DebugGroupsSignalListenerProperties properties) {
        this.properties = properties;
    }
}
