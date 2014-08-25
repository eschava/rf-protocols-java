package rf.protocols.core;

import org.junit.Assert;
import org.junit.Test;
import rf.protocols.core.impl.BitPacket;

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
        for (byte b : message.getBytes())
            packet.addBit(b == '1');

        Assert.assertEquals(7, packet.getInt(1,3));
        Assert.assertEquals(7, packet.getInt(3,1));
        Assert.assertEquals(5, packet.getInt(4, 7));
        Assert.assertEquals(10, packet.getInt(7, 4));
    }
}
