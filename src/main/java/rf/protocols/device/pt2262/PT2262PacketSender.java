package rf.protocols.device.pt2262;

import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.external.ognl.PropertiesConfigurer;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PT2262PacketSender implements PacketSender<BitPacket>, Cloneable {
    private PT2262SignalListenerProperties properties = new PT2262SignalListenerProperties();
    private PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);

    public PT2262SignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(PT2262SignalListenerProperties properties) {
        this.properties = properties;
        propertiesConfigurer = new PropertiesConfigurer(properties);
    }

    @Override
    public void setProperty(String property, String value) {
        propertiesConfigurer.setProperty(property, value);
    }

    public void send(BitPacket packet, SignalLengthSender signalSender) {
        sendSeparator(signalSender);

        for (int i = 0; i < properties.repeats; i++) {
            for (int j = 0; j < packet.getSize(); j++) {
                boolean bit = packet.getBit(j);
                if (bit)
                    send1Signal(signalSender);
                else
                    send0Signal(signalSender);
            }

            sendSeparator(signalSender);
        }
    }

    private void send0Signal(SignalLengthSender signalSender) {
        signalSender.send(true,  (long) (properties.separatorLength * properties.longSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.shortSignalFraction));
    }

    private void send1Signal(SignalLengthSender signalSender) {
        signalSender.send(false, (long) (properties.separatorLength * properties.shortSignalFraction));
        signalSender.send(true,  (long) (properties.separatorLength * properties.longSignalFraction));
    }

    private void sendSeparator(SignalLengthSender signalSender) {
        signalSender.send(true, properties.separatorLength);
    }

    @Override
    public PT2262PacketSender clone(String newName) {
        try {
            PT2262PacketSender clone = (PT2262PacketSender) super.clone();
            clone.properties = (PT2262SignalListenerProperties) properties.clone();
            clone.propertiesConfigurer = new PropertiesConfigurer(clone.properties);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
