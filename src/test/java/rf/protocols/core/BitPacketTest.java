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
        BitPacket packet = BitPacket.valueOfBinString(message, 20);

        assertEquals(7, packet.getInt(1, 3));
        assertEquals(7, packet.getInt(3, 1));
        assertEquals(5, packet.getInt(4, 7));
        assertEquals(10, packet.getInt(7, 4));
    }

    @Test
    public void testToString() throws Exception {
        String hex = "62605F1810207E670000";
        BitPacket packet = BitPacket.valueOfHexString(hex, 100);
        assertEquals(hex.toLowerCase(), packet.toString());

        packet = BitPacket.valueOfHexString(hex, 500);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "62605F1810207E67000"; // nibble
        packet = BitPacket.valueOfHexString(hex, 500);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "62605F1810207E67005"; // nibble
        packet = BitPacket.valueOfHexString(hex, 500);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "62"; // byte
        packet = BitPacket.valueOfHexString(hex, 500);
        assertEquals(hex.toLowerCase(), packet.toString());

        hex = "6"; // nibble
        packet = BitPacket.valueOfHexString(hex, 500);
        assertEquals(hex.toLowerCase(), packet.toString());

        String bin = "1"; // bits
        packet = BitPacket.valueOfBinString(bin, 500);
        assertEquals("1", packet.toString());
    }
}
