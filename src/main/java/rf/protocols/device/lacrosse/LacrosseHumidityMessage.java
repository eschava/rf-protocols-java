package rf.protocols.device.lacrosse;

import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.HumidityMessage;

/**
 * http://www.f6fbb.org/domo/sensors/tx3_th.php
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseHumidityMessage extends LacrosseAbstractMessage
        implements HumidityMessage {

    public static int TYPE = 14;
    private static final MessageMetaData METADATA = new LacrosseHumidityMessageMetaData();

    public LacrosseHumidityMessage(BitPacket packet) {
        super(packet);
    }

    @Override
    public MessageMetaData getMetaData() {
        return METADATA;
    }

    @Override
    public double getHumidity() {
        return packet.getInt(20, 23) * 10.0d + packet.getInt(24, 27) + packet.getInt(28, 31) / 10.0d;
    }
}
