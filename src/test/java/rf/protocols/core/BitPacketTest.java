package rf.protocols.core;

import org.junit.Test;
import rf.protocols.core.impl.BitPacket;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link rf.protocols.core.impl.BitPacket}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BitPacketTest {
    @Test
    public void testGetInt() throws Exception {
        String message = "01110101";
        BitPacket packet = new BitPacket(20);
        fillFromBinString(message, packet);

        assertEquals(7, packet.getInt(1, 3));
        assertEquals(7, packet.getInt(3, 1));
        assertEquals(5, packet.getInt(4, 7));
        assertEquals(10, packet.getInt(7, 4));
    }

    @Test
    public void testToString() throws Exception {
        String hex = "62605F1810207E670000";
        BitPacket packet = new BitPacket(100);
        fillFromHexString(hex, packet);
        assertEquals(hex.toLowerCase(), packet.toString());

        packet = new BitPacket(500);
        fillFromHexString(hex, packet);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "62605F1810207E67000"; // nibble
        packet = new BitPacket(500);
        fillFromHexString(hex, packet);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "62605F1810207E67005"; // nibble
        packet = new BitPacket(500);
        fillFromHexString(hex, packet);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "62"; // byte
        packet = new BitPacket(500);
        fillFromHexString(hex, packet);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "6"; // nibble
        packet = new BitPacket(500);
        fillFromHexString(hex, packet);
        assertEquals(hex.toLowerCase(), packet.toString());

        String bin = "1"; // bits
        packet = new BitPacket(500);
        fillFromBinString(bin, packet);
        assertEquals("1", packet.toString());
    }

    private static void fillFromBinString(String bin, BitPacket packet) {
        for (byte b : bin.getBytes())
            packet.addBit(b == '1');
    }

    private static void fillFromHexString(String hex, BitPacket packet) {
        for (int i = 0; i < hex.length(); i += 2) {
            boolean nibble = hex.length() - i == 1;
            String bs = hex.substring(i, i + (nibble ? 1 : 2));
            int b = Integer.parseInt(bs, 16);
            for (int j = 0; j < (nibble ? 4 : 8); j++) {
                packet.addBit((b & 1) == 1);
                b >>= 1;
            }
        }
    }
}
