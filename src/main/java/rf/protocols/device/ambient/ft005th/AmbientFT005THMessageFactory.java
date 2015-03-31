package rf.protocols.device.ambient.ft005th;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientFT005THMessageFactory implements MessageFactory<BitPacket, AmbientFT005THMessage> {
    private final String name;

    public AmbientFT005THMessageFactory(String name) {
        this.name = name;
    }

    @Override
    public AmbientFT005THMessage createMessage(BitPacket packet) {
        AmbientFT005THMessage message = new AmbientFT005THMessage(packet);
        message.setProtocol(name);
        return message;
    }
}
