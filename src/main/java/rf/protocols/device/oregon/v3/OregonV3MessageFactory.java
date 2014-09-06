package rf.protocols.device.oregon.v3;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV3MessageFactory implements MessageFactory<BitPacket, OregonV3Message> {
    private final String name;

    public OregonV3MessageFactory(String name) {
        this.name = name;
    }

    @Override
    public OregonV3Message createMessage(BitPacket packet) {
        OregonV3Message message = new OregonV3Message(packet);
        message.setProtocol(name);
        return message;
    }
}
