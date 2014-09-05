package rf.protocols.analysis.intervals;

import rf.protocols.core.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsPacket implements Packet {

    private long beforePacketLength;
    private long afterPacketLength;
    private List<String> intervals = new ArrayList<String>();

    public IntervalsPacket() {
        clear();
    }

    public void clear() {
        beforePacketLength = afterPacketLength = -1;
        intervals.clear();
    }

    public void setBeforePacketLength(long beforePacketLength) {
        this.beforePacketLength = beforePacketLength;
    }

    public void setAfterPacketLength(long afterPacketLength) {
        this.afterPacketLength = afterPacketLength;
    }

    public void addInterval(String interval) {
        intervals.add(interval);
    }

    public long getBeforePacketLength() {
        return beforePacketLength;
    }

    public long getAfterPacketLength() {
        return afterPacketLength;
    }

    public List<String> getIntervals() {
        return intervals;
    }

    @Override
    public Packet clone() {
        throw new UnsupportedOperationException();
    }
}
