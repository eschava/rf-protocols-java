package rf.protocols.device.oregon.v2.message;

import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.BatteryStatusMessage;
import rf.protocols.core.message.ChannelIdMessage;
import rf.protocols.core.message.DeviceTypeMessage;
import rf.protocols.core.message.RollingIdMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public abstract class OregonV2AbstractMessage extends AbstractMessage<BitPacket>
        implements DeviceTypeMessage, ChannelIdMessage, RollingIdMessage, BatteryStatusMessage {

    public static int getType(BitPacket packet) {
        return (packet.getInt(7, 4) << 12) + (packet.getInt(11, 8) << 8) +
                (packet.getInt(15, 12) << 4) + (packet.getInt(19, 16));
    }

    public OregonV2AbstractMessage(String protocol, BitPacket packet) {
        super(protocol, packet);
    }

    protected abstract int getMessageSize();

    @Override
    public boolean isValid() {
        return /*sync*/ packet.getInt(3, 0) == 10 && packet.getSize() >= getMessageSize() && isCrcValid();
    }

    public boolean isCrcValid() {
        return getCheckSum() == calculateCheckSum();
    }

    public int getCheckSum() {
        return packet.getInt(getMessageSize() - 1, getMessageSize() - 8);
    }

    public int calculateCheckSum() {
        int sum = 0;
        for (int i = 4; i < getMessageSize() - 9; i += 4)
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
        return (packet.getInt(35, 32) & 0x4) > 0;
    }
}
