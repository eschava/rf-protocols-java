package rf.protocols.device.oregon.v2;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2MessageFactory implements MessageFactory<BitPacket, OregonV2Message> {
    private final String name;

    public OregonV2MessageFactory(String name) {
        this.name = name;
    }

    @Override
    public OregonV2Message createMessage(BitPacket packet) {
        OregonV2Message message = new OregonV2Message(packet);
        message.setProtocol(name);
        return message;
    }
}
