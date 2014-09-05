package rf.protocols.remoteswitch;

import rf.protocols.core.Packet;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchPacket implements Packet, Cloneable {
    private long value;

    public RemoteSwitchPacket(long value) {
        this.value = value;
    }

    public RemoteSwitchPacket(String value) {
        this.value = Long.parseLong(value, 3);
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value, 3);
    }

    @Override
    public Packet clone() {
        try {
            return (Packet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
