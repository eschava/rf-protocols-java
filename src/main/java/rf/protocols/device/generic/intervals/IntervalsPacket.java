package rf.protocols.device.generic.intervals;

import rf.protocols.core.Packet;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsPacket implements Packet, Cloneable {

    private StringBuilder names = new StringBuilder();
    private int size;

    public IntervalsPacket() {
        clear();
    }

    public IntervalsPacket(String value) {
        clear();
        names.append(value);
    }

    public void clear() {
        names.setLength(0);
        size = 0;
    }

    public void addInterval(String name) {
        names.append(name);
        size++;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return names.toString();
    }

    @Override
    public IntervalsPacket clone() {
        try {
            IntervalsPacket clone = (IntervalsPacket) super.clone();
            clone.names = new StringBuilder(names);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
