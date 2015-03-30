package rf.protocols.device.lacrosse;

import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.message.TemperatureMessage;

/**
 * http://www.f6fbb.org/domo/sensors/tx3_th.php
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseTemperatureMessage extends LacrosseAbstractMessage
        implements TemperatureMessage {

    public static int TYPE = 0;
    private static final MessageMetaData METADATA = new LacrosseTemperatureMessageMetaData();

    public LacrosseTemperatureMessage(BitPacket packet) {
        super(packet);
    }

    @Override
    public MessageMetaData getMetaData() {
        return METADATA;
    }

    @Override
    public double getTemperature() {
        return (packet.getInt(20, 23) - 5) * 10.0d + packet.getInt(24, 27) + packet.getInt(28, 31) / 10.0d;
    }
}
