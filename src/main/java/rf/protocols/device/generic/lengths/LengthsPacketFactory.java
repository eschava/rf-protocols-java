package rf.protocols.device.generic.lengths;

import rf.protocols.core.PacketFactory;
import rf.protocols.core.message.StringMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LengthsPacketFactory implements PacketFactory<LengthsPacket, StringMessage> {
    @Override
    public LengthsPacket createPacket(StringMessage message) {
        String str = message.getValue();
        String[] parts = str.split(",");
        int[] lengths = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            lengths[i] = Integer.parseInt(parts[i]);
        }
        return new LengthsPacket(lengths);
    }

    @Override
    public void setProperty(String property, String value) {
    }

    @Override
    public PacketFactory<LengthsPacket, StringMessage> clone(String newName) {
        return this; // no state
    }
}
