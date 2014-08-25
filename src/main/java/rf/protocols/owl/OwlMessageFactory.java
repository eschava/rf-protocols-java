package rf.protocols.owl;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlMessageFactory implements MessageFactory<BitPacket, OwlMessage> {
    private final String name;

    public OwlMessageFactory(String name) {
        this.name = name;
    }

    @Override
    public OwlMessage createMessage(BitPacket packet) {
        OwlMessage message = new OwlMessage(packet);
        message.setName(name);
        return message;
    }
}
