package rf.protocols.core.impl;

import rf.protocols.core.Packet;

import java.util.BitSet;

/**
 * Implementation of {@link Packet} using {@link java.util.BitSet} to store data bits
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BitPacket implements Packet, Cloneable {
    private BitSet bitSet;
    private int size = 0;

    public BitPacket(int maxBits) {
        bitSet = new BitSet(maxBits);
    }

    public void clear() {
        if (size > 0) {
            bitSet.clear();
            size = 0;
        }
    }

    public int getSize() {
        return size;
    }

    public void addBit(boolean bit) {
        bitSet.set(size++, bit);
    }

    public boolean getBit(int n) {
        return bitSet.get(n);
    }

    public byte[] getBytes() {
        return bitSet.toByteArray();
    }

    public int getInt(int fromBit, int toBit) {
        int result = 0;

        if (fromBit <= toBit) {
            for (int i = fromBit; i <= toBit ; i ++)
                result = result * 2 + (getBit(i) ? 1 : 0);
        } else {
            for (int i = fromBit; i >= toBit ; i--)
                result = result * 2 + (getBit(i) ? 1 : 0);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b1 : getBytes()) {
            int i = b1;
            if (i < 0) i+=256;
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

    @Override
    public BitPacket clone() {
        try {
            BitPacket result = (BitPacket) super.clone();
            result.bitSet = (BitSet) bitSet.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
