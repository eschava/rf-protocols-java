package rf.protocols.device.remoteswitch;

import rf.protocols.core.Packet;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchPacket implements Packet, Cloneable {
    private int size;
    private long value;

    public RemoteSwitchPacket(int size, long value) {
        this.size = size;
        this.value = value;
    }

    public RemoteSwitchPacket(String value) {
        this.size = value.length();
        this.value = Long.parseLong(value, 3);
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        String s = Long.toString(value, 3);
        // add leading zeros
        while (s.length() < size)
            s = "0" + s;
        return s;
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
