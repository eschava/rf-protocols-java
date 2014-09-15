package rf.protocols.device.generic.intervals;

import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.external.ognl.PropertiesConfigurer;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsPacketSender implements PacketSender<IntervalsPacket>, Cloneable {
    private IntervalsSignalListenerProperties properties = new IntervalsSignalListenerProperties();
    private PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);

    public IntervalsSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(IntervalsSignalListenerProperties properties) {
        this.properties = properties;
        propertiesConfigurer = new PropertiesConfigurer(properties);
    }

    @Override
    public void setProperty(String property, String value) {
        propertiesConfigurer.setProperty(property, value);
    }

    public void send(IntervalsPacket packet, SignalLengthSender signalSender) {
        String message = packet.toString();
        Map<String, Long> intervalLengths = properties.getIntervalLengthsMap();
        long separatorLength = properties.separatorInterval.getMed();
        boolean level = properties.separatorIsHigh;

        signalSender.send(level, separatorLength);

        for (int i = 0; i < properties.repeats; i++) {
            for (char ch : message.toCharArray()) {
                Long l = intervalLengths.get(String.valueOf(ch));
                if (l != null) {
                    level = !level;
                    signalSender.send(level, l);
                }
            }

            level = !level;
            signalSender.send(level, separatorLength);
        }
    }

    @Override
    public PacketSender<IntervalsPacket> clone(String newName) {
        try {
            IntervalsPacketSender clone = (IntervalsPacketSender) super.clone();
            clone.properties = (IntervalsSignalListenerProperties) properties.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
