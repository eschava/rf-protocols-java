package rf.protocols.owl;

import org.junit.Test;
import rf.protocols.core.impl.BitPacket;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlTest {
    private static final String PACKET = "62605F1810207E670000";

    @Test
    public void testMessage() throws Exception {
        BitPacket packet = new BitPacket(100);
        for (int i = 0; i < PACKET.length(); i += 2) {
            String bs = PACKET.substring(i, i + 2);
            int b = Integer.parseInt(bs, 16);
            for (int j = 0; j < 8; j++) {
                packet.addBit((b & 1) == 1);
                b >>= 1;
            }
        }

        OwlMessage message = new OwlMessage(packet);
        assertEquals("6260", message.getDeviceType());
        assertEquals(4137, message.getCurrentPower(), 0d);
        assertEquals(1896, message.getTotalPower(), 1d);
    }
}
