package rf.protocols.device.generic.lengths;

import rf.protocols.core.Packet;

import java.util.Arrays;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LengthsPacket implements Packet, Cloneable {

    private int[] lengths;

    public LengthsPacket(int[] lengths) {
        this.lengths = lengths;
    }

    public int[] getLengths() {
        return lengths;
    }

    public int getSize() {
        return lengths.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(lengths);
    }

    @Override
    public LengthsPacket clone() {
        try {
            LengthsPacket clone = (LengthsPacket) super.clone();
            clone.lengths = lengths.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
