package rf.protocols.device.lacrosse;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseMessageFactory implements MessageFactory<BitPacket, LacrosseAbstractMessage> {
    private final String name;

    public LacrosseMessageFactory(String name) {
        this.name = name;
    }

    @Override
    public LacrosseAbstractMessage createMessage(BitPacket packet) {
        if (LacrosseAbstractMessage.getPrefix(packet) != LacrosseAbstractMessage.PREFIX)
            return null;

        LacrosseAbstractMessage message;
        int type = LacrosseAbstractMessage.getType(packet);
        if (type == LacrosseTemperatureMessage.TYPE)
            message = new LacrosseTemperatureMessage(packet);
        else if (type == LacrosseHumidityMessage.TYPE)
            message = new LacrosseHumidityMessage(packet);
        else
            return null;

        message.setProtocol(name);
        return message;
    }
}
