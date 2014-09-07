package rf.protocols.device.oregon.v2;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.device.oregon.v2.message.OregonV2AbstractMessage;
import rf.protocols.device.oregon.v2.message.OregonV2TemperatureHumidityMessage;
import rf.protocols.device.oregon.v2.message.OregonV2TemperatureMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2MessageFactory implements MessageFactory<BitPacket, OregonV2AbstractMessage> {
    private final String name;

    public OregonV2MessageFactory(String name) {
        this.name = name;
    }

    @Override
    public OregonV2AbstractMessage createMessage(BitPacket packet) {
        OregonV2AbstractMessage message = null;
        int type = (packet.getInt(7, 4) << 12) + (packet.getInt(11, 8) << 8) +
                (packet.getInt(15, 12) << 4) + (packet.getInt(19, 16));

        if (oneOf(type, OregonV2TemperatureMessage.DEVICE_TYPES))
            message = new OregonV2TemperatureMessage(name, packet);
        else if (oneOf(type, OregonV2TemperatureHumidityMessage.DEVICE_TYPES))
            message = new OregonV2TemperatureHumidityMessage(name, packet);

        return message;
    }

    private static boolean oneOf(int type, int[] deviceTypes) {
        for (int deviceType : deviceTypes)
            if (deviceType == type)
                return true;
        return false;
    }
}
