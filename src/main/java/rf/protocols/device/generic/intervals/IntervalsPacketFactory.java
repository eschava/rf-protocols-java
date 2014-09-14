package rf.protocols.device.generic.intervals;

import rf.protocols.core.PacketFactory;
import rf.protocols.core.message.StringMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsPacketFactory implements PacketFactory<IntervalsPacket, StringMessage> {
    @Override
    public IntervalsPacket createPacket(StringMessage message) {
        return new IntervalsPacket(message.getValue());
    }

    @Override
    public void setProperty(String property, String value) {
    }

    @Override
    public PacketFactory<IntervalsPacket, StringMessage> clone(String newName) {
        return this; // no state
    }
}
