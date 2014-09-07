package rf.protocols.device.owl;

import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.DeviceTypeMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlMessage extends AbstractMessage<BitPacket> implements DeviceTypeMessage {
    public static final String PROTOCOL = "Owl";

    private static final MessageMetaData METADATA = new OwlMessageMetaData();

    public OwlMessage(BitPacket packet) {
        super(PROTOCOL, packet);
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
//        throw new UnsupportedOperationException();
//    }

    public boolean isCrcValid() {
        return true; // TODO
    }

    public String getDeviceType() {
        return Integer.toHexString(packet.getInt(7, 0)) + Integer.toHexString(packet.getInt(15, 8));
    }

    public double getCurrentPower() {
        int currentPower = packet.getInt(39, 28) * 16;
        currentPower += currentPower / 160; // to match displayed value
        return currentPower;
    }

    public double getTotalPower() {
        int totalPower = packet.getInt(79, 44);
        return totalPower / 223.666;
    }
}
