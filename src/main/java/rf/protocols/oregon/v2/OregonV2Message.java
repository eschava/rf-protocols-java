package rf.protocols.oregon.v2;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.message.HumidityMessage;
import rf.protocols.core.message.TemperatureMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2Message extends AbstractMessage<BitPacket>
        implements TemperatureMessage, HumidityMessage {

    public static final String NAME = "OregonV2";

    private static final MessageMetaData METADATA = new OregonV2MessageMetaData();

    public OregonV2Message(BitPacket packet) {
        super(NAME, packet);
    }

    @Override
    public MessageMetaData<Message> getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return isLengthValid() && isCrcValid();
    }

    public boolean isLengthValid() {
        throw new UnsupportedOperationException();
    }

    public boolean isCrcValid() {
        return true; // TODO
    }

    public double getTemperature() {
        throw new UnsupportedOperationException();
    }

    public double getHumidity() {
        throw new UnsupportedOperationException();
    }
}
