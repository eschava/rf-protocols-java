package rf.protocols.device.pt2262;

import rf.protocols.core.PacketFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.StringMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PT2262PacketFactory implements PacketFactory<BitPacket, StringMessage> {
    @Override
    public BitPacket createPacket(StringMessage message) {
        return BitPacket.valueOfHexString(message.getValue(), 100);
    }

    @Override
    public void setProperty(String property, String value) {
    }

    @Override
    public PacketFactory<BitPacket, StringMessage> clone(String newName) {
        return this; // no state
    }
}
