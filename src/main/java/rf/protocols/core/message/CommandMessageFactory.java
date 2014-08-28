package rf.protocols.core.message;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class CommandMessageFactory implements MessageFactory<BitPacket, CommandMessage> {
    private String name;

    public CommandMessageFactory(String name) {
        this.name = name;
    }

    @Override
    public CommandMessage createMessage(BitPacket packet) {
        return new CommandMessage(name, packet);
    }
}
