package rf.protocols.device.oregon.v2;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.*;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2Message extends AbstractMessage<BitPacket>
        implements DeviceTypeMessage, ChannelIdMessage, RollingIdMessage, BatteryStatusMessage,
        TemperatureMessage, HumidityMessage {

    public static final String PROTOCOL = "OregonV2";

    private static final MessageMetaData METADATA = new OregonV2MessageMetaData();

    public OregonV2Message(BitPacket packet) {
        super(PROTOCOL, packet);
    }

    @Override
    public MessageMetaData<Message> getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return /*sync*/ packet.getInt(3, 0) == 10 && isCrcValid();
    }

    public boolean isCrcValid() {
        return getCheckSum() == calculateCheckSum();
    }

    public int getCheckSum() {
        return packet.getInt(59, 52);
    }

    public int calculateCheckSum() {
        int sum = 0;
        for (int i = 4; i < 51; i += 4)
            sum += packet.getInt(i + 3, i);
        return sum % 256;
    }

    public String getDeviceType() {
        return packet.getHex(7, 4) + packet.getHex(11, 8) + packet.getHex(15, 12) + packet.getHex(19, 16);
    }

    public int getChannelId() {
        return packet.getInt(23, 20);
    }

    public int getRollingId() {
        return packet.getInt(31, 24);
    }

    public boolean isBatteryLow() {
        return (packet.getInt(35, 22) & 0x4) > 0;
    }

    public double getTemperature() {
        double abs = packet.getInt(47, 44) * 10 + packet.getInt(43, 40) + packet.getInt(39, 36) / 10d;
        int sign = packet.getInt(51, 48);
        return sign == 0 ? abs : -abs;
    }

    public double getHumidity() {
        throw new UnsupportedOperationException();
    }
}
