package rf.protocols.device.generic.lengths;

import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.external.ognl.PropertiesConfigurer;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LengthsPacketSender implements PacketSender<LengthsPacket>, Cloneable {
    private LengthsProperties properties = new LengthsProperties();
    private PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);

    public LengthsProperties getProperties() {
        return properties;
    }

    public void setProperties(LengthsProperties properties) {
        this.properties = properties;
        propertiesConfigurer = new PropertiesConfigurer(properties);
    }

    @Override
    public void setProperty(String property, String value) {
        propertiesConfigurer.setProperty(property, value);
    }

    public void send(LengthsPacket packet, SignalLengthSender signalSender) {
        for (int i = 0; i < properties.repeats; i++) {
            boolean level = properties.firstIsHigh;

            for (int l : packet.getLengths()) {
                signalSender.send(level, l);
                level = !level;
            }
        }
    }

    @Override
    public PacketSender<LengthsPacket> clone(String newName) {
        try {
            LengthsPacketSender clone = (LengthsPacketSender) super.clone();
            clone.properties = (LengthsProperties) properties.clone();
            clone.propertiesConfigurer = new PropertiesConfigurer(clone.properties);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
