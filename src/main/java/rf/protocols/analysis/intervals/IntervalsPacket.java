package rf.protocols.analysis.intervals;

import rf.protocols.core.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsPacket implements Packet, Cloneable {

    private long beforePacketLength;
    private long afterPacketLength;
    private List<Long> lengths = new ArrayList<Long>();
    private boolean firstSignalLevel;

    public IntervalsPacket() {
        clear();
    }

    public void clear() {
        beforePacketLength = afterPacketLength = -1;
        firstSignalLevel = false;
        lengths.clear();
    }

    public void setBeforePacketLength(long beforePacketLength) {
        this.beforePacketLength = beforePacketLength;
    }

    public void setAfterPacketLength(long afterPacketLength) {
        this.afterPacketLength = afterPacketLength;
    }

    public void setFirstSignalLevel(boolean firstSignalLevel) {
        this.firstSignalLevel = firstSignalLevel;
    }

    public boolean getFirstSignalLevel() {
        return firstSignalLevel;
    }

    public void addLength(long length) {
        lengths.add(length);
    }

    public long getBeforePacketLength() {
        return beforePacketLength;
    }

    public long getAfterPacketLength() {
        return afterPacketLength;
    }

    public List<Long> getLengths() {
        return lengths;
    }

    @Override
    public IntervalsPacket clone() {
        try {
            IntervalsPacket clone = (IntervalsPacket) super.clone();
            clone.lengths = new ArrayList<Long>(lengths);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
