package rf.protocols.device.ambient.ws05;

import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.HumidityMessage;
import rf.protocols.core.message.RollingIdMessage;
import rf.protocols.core.message.TemperatureMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientWS05Message extends AbstractMessage<BitPacket>
        implements RollingIdMessage, TemperatureMessage, HumidityMessage {

    public static final String PROTOCOL = "AmbientWS05";

    private static final MessageMetaData METADATA = new AmbientWS05MessageMetaData();

    public AmbientWS05Message(BitPacket packet) {
        super(PROTOCOL, packet);
    }

    @Override
    public MessageMetaData getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return isCrcValid();
    }

    public boolean isCrcValid() {
        return getCheckSum() == calculateCheckSum();
    }

    public int getCheckSum() {
        return packet.getInt(40, 47);
    }

    public int calculateCheckSum() {
        int sum = 0;
        for (int i = 0; i < packet.getSize() - 8; i += 8)
            sum ^= packet.getInt(i, i + 7); // xor all bytes
        return sum;
    }

    public double getTemperature() {
        return (packet.getInt(27, 16) - 400) / 10.0d;
    }

    public double getHumidity() {
        return packet.getInt(39, 32);
    }

    @Override
    public int getRollingId() {
        return packet.getInt(8, 15);
    }
}
