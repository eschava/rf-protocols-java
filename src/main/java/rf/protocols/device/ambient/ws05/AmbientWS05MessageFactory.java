package rf.protocols.device.ambient.ws05;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientWS05MessageFactory implements MessageFactory<BitPacket, AmbientWS05Message> {
    private final String name;

    public AmbientWS05MessageFactory(String name) {
        this.name = name;
    }

    @Override
    public AmbientWS05Message createMessage(BitPacket packet) {
        AmbientWS05Message message = new AmbientWS05Message(packet);
        message.setProtocol(name);
        return message;
    }
}
