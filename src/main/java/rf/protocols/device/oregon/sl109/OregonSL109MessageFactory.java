package rf.protocols.device.oregon.sl109;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109MessageFactory implements MessageFactory<BitPacket, OregonSL109Message> {
    private final String name;

    public OregonSL109MessageFactory(String name) {
        this.name = name;
    }

    @Override
    public OregonSL109Message createMessage(BitPacket packet) {
        OregonSL109Message message = new OregonSL109Message(packet);
        message.setName(name);
        return message;
    }
}
