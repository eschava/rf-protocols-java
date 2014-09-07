package rf.protocols.device.oregon.v2.message;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.HumidityMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2TemperatureHumidityMessage extends OregonV2TemperatureMessage
        implements HumidityMessage {

    public static final int[] DEVICE_TYPES = {0x1D20, 0xF824, 0xF8B4};

    private static final MessageMetaData METADATA = new OregonV2TemperatureHumidityMessageMetaData();

    public OregonV2TemperatureHumidityMessage(String protocol, BitPacket packet) {
        super(protocol, packet);
    }

    @Override
    protected int getMessageSize() {
        return 72;
    }

    @Override
    public MessageMetaData<Message> getMetaData() {
        return METADATA;
    }

    @Override
    public double getHumidity() {
        return packet.getInt(59, 56) * 10 + packet.getInt(55, 52);
    }
}
