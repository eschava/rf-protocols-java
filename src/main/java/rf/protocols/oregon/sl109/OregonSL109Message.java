package rf.protocols.oregon.sl109;

import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.message.HumidityMessage;
import rf.protocols.core.message.TemperatureMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109Message extends AbstractMessage<BitPacket>
        implements TemperatureMessage, HumidityMessage {

    public static final String NAME = "OregonSL109";

    private static final MessageMetaData METADATA = new OregonSL109MessageMetaData();

    public OregonSL109Message(BitPacket packet) {
        super(NAME, packet);
    }

    @Override
    public MessageMetaData getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return isLengthValid() && isCrcValid();
    }

    public boolean isLengthValid() {
        return packet.getSize() == 38;
    }

    public boolean isCrcValid() {
        return true; // TODO
    }

    public double getTemperature() {
        return (256 * packet.getInt(21, 18) + 16 * packet.getInt(17, 14) + packet.getInt(29, 26)) / 10.0d;
    }

    public double getHumidity() {
        return packet.getInt(13, 10) * 10 + packet.getInt(9, 6);
    }
}
