package rf.protocols.device.owl;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlMessageFactory implements MessageFactory<BitPacket, OwlMessage> {
    private final String protocol;

    public OwlMessageFactory(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public OwlMessage createMessage(BitPacket packet) {
        OwlMessage message = new OwlMessage(packet);
        message.setProtocol(protocol);
        return message;
    }
}
