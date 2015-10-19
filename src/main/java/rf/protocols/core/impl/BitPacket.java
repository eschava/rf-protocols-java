package rf.protocols.core.impl;

import rf.protocols.core.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        long[] words = bitSet.toLongArray();
        if (words.length == 0)
            return new byte[0];

        byte[] bytes = new byte[(size + 7) / 8];
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < words.length - 1; i++)
            bb.putLong(words[i]);

        long x = words[words.length - 1];
        int bytesLeft = ((size - (words.length - 1) * 64) + 7) / 8;
        for (int i = 0 ; i < bytesLeft; i++) {
            bb.put((byte) (x & 0xff));
            x >>>= 8;
        }
        return bytes;
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

    public String getHex(int fromBit, int toBit) {
        StringBuilder sb = new StringBuilder();

        if (fromBit <= toBit) {
            for (int i = fromBit; i <= toBit ; i += 4) {
                int b = getInt(i, i + 3);
                sb.append(Integer.toHexString(b));
            }
        } else {
            for (int i = fromBit; i >= toBit ; i -= 4) {
                int b = getInt(i, i - 3);
                sb.append(Integer.toHexString(b));
            }
        }

        return sb.toString();
    }

    public String getBin(int fromBit, int toBit) {
        StringBuilder sb = new StringBuilder();

        if (fromBit <= toBit) {
            for (int i = fromBit; i <= toBit; i ++) {
                boolean bit = getBit(i);
                sb.append(bit ? '1' : '0');
            }
        } else {
            for (int i = fromBit; i >= toBit ; i--) {
                boolean bit = getBit(i);
                sb.append(bit ? '1' : '0');
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
//        return getHex(0, size - 1);
        StringBuilder sb = new StringBuilder();
        int c = 0;
        for (byte b1 : getBytes()) {
            int i = b1;
            if (i < 0) i+=256;
            boolean nibble = size - c <= 4;
            String hex = Integer.toHexString(i);
            if (!nibble && hex.length() == 1)
                sb.append('0');
            sb.append(hex);
            c += 8;
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

    public int indexOf(int intValue, int sizeToSearch) {
        for (int i = 0; i < size - sizeToSearch; i++) {
            if (getInt(i, i + sizeToSearch - 1) == intValue)
                return i;
        }
        return -1;
    }

    public BitPacket subpacket(int startIndex, int packetSize) {
        BitPacket packet = new BitPacket(packetSize);
        packet.size = packetSize;
        for (int i = 0; i < packetSize; i++) {
            if (bitSet.get(i + startIndex))
                packet.bitSet.set(i);
        }
        return packet;
    }

    public static BitPacket valueOfBinString(String bin, int maxBits) {
        BitPacket packet = new BitPacket(maxBits);
        for (byte b : bin.getBytes())
            packet.addBit(b == '1');
        return packet;
    }

    public static BitPacket valueOfHexString(String hex, int maxBits) {
        BitPacket packet = new BitPacket(maxBits);
        for (int i = 0; i < hex.length(); i += 2) {
            boolean nibble = hex.length() - i == 1;
            String bs = hex.substring(i, i + (nibble ? 1 : 2));
            int b = Integer.parseInt(bs, 16);
            for (int j = 0; j < (nibble ? 4 : 8); j++) {
                packet.addBit((b & 1) == 1);
                b >>= 1;
            }
        }
        return packet;
    }
}
