package rf.protocols.core.message;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.Packet;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class StringMessageFactory<P extends Packet> implements MessageFactory<P, StringMessage> {
    private String name;

    public StringMessageFactory(String name) {
        this.name = name;
    }

    @Override
    public StringMessage createMessage(P packet) {
        return new StringMessage(name, packet.toString());
    }
}
