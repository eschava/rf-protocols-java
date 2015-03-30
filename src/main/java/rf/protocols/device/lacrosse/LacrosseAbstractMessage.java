package rf.protocols.device.lacrosse;

import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.RollingIdMessage;

/**
 * http://www.f6fbb.org/domo/sensors/tx3_th.php
 * @author Eugene Schava <eschava@gmail.com>
 */
public abstract class LacrosseAbstractMessage extends AbstractMessage<BitPacket>
        implements RollingIdMessage {

    public static int PREFIX = 10;
    public static final String PROTOCOL = "Lacrosse";

    public static int getPrefix(BitPacket packet) {
        return packet.getInt(0, 7);
    }

    public static int getType(BitPacket packet) {
        return packet.getInt(8, 11);
    }

    public LacrosseAbstractMessage(BitPacket packet) {
        super(PROTOCOL, packet);
    }

    @Override
    public boolean isValid() {
        return isCrcValid();
    }

    public boolean isCrcValid() {
        return getCheckSum() == calculateCheckSum();
    }

    public int getCheckSum() {
        return packet.getInt(40, 43);
    }

    public int calculateCheckSum() {
        int sum = 0;
        for (int i = 0; i < packet.getSize() - 4; i += 4)
            sum += packet.getInt(i, i + 3);
        return sum % 16;
    }

    @Override
    public int getRollingId() {
        return packet.getInt(12, 18);
    }
}
