package rf.protocols.device.oregon.sl109;

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
        return /*isLengthValid() && */isCrcValid();
    }

//    public boolean isLengthValid() {
//        return packet.getSize() == 38;
//    }

    public boolean isCrcValid() {
        return getCheckSum() == calculateCheckSum();
    }

    public int getCheckSum() {
        return packet.getInt(0, 3);
    }

    public int calculateCheckSum() {
        int sum = packet.getInt(4, 5);
        for (int i = 6; i < packet.getSize(); i += 4)
            sum += packet.getInt(i, i + 3);
        return sum % 16;
    }

    public int getChannelId() {
        return packet.getInt(4, 5);
    }

    public double getTemperature() {
        return (256 * packet.getInt(14, 17) + 16 * packet.getInt(18, 21) + packet.getInt(22, 25)) / 10.0d;
    }

    public double getHumidity() {
        return packet.getInt(6, 9) * 10 + packet.getInt(10, 13);
    }

    public int getStatus() {
        return packet.getInt(26, 29);
    }

    public int getRollingCode() {
        return packet.getInt(30, 37);
    }
}
