package rf.protocols.device.oregon.v2.message;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.*;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2TemperatureMessage extends OregonV2AbstractMessage
        implements TemperatureMessage {

    public static final int[] DEVICE_TYPES = {0xEC40, 0xC844};

    private static final MessageMetaData METADATA = new OregonV2TemperatureMessageMetaData();

    public OregonV2TemperatureMessage(String protocol, BitPacket packet) {
        super(protocol, packet);
    }

    @Override
    public MessageMetaData<Message> getMetaData() {
        return METADATA;
    }

    @Override
    protected int getMessageSize() {
        return 60;
    }

    public double getTemperature() {
        double abs = packet.getInt(47, 44) * 10 + packet.getInt(43, 40) + packet.getInt(39, 36) / 10d;
        int sign = packet.getInt(51, 48);
        return sign == 0 ? abs : -abs;
    }
}
