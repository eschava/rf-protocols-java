package rf.protocols.device.generic.intervalsequence;

import rf.protocols.core.PacketFactory;
import rf.protocols.external.ognl.PropertiesConfigurer;
import rf.protocols.core.message.StringMessage;
import rf.protocols.device.generic.intervals.IntervalsPacket;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalSequencePacketFactory implements PacketFactory<IntervalsPacket, StringMessage>, Cloneable {
    private IntervalSequenceProtocolProperties properties = new IntervalSequenceProtocolProperties();
    private PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);

    @Override
    public IntervalsPacket createPacket(StringMessage message) {
        String value = message.getValue();
        Map<String, String> nameToSequenceMap = properties.createNameToSequenceMap();

        StringBuilder packet = new StringBuilder();
        packet.append(properties.prefix);
        for (char ch : value.toCharArray()) {
            String sequence = nameToSequenceMap.get(String.valueOf(ch));
            packet.append(sequence);
        }
        packet.append(properties.postfix);

        return new IntervalsPacket(packet.toString());
    }

    @Override
    public void setProperty(String property, String value) {
        propertiesConfigurer.setProperty(property, value);
    }

    @Override
    public PacketFactory<IntervalsPacket, StringMessage> clone(String newName) {
        try {
            IntervalSequencePacketFactory clone = (IntervalSequencePacketFactory) super.clone();
            clone.properties = (IntervalSequenceProtocolProperties) properties.clone();
            clone.propertiesConfigurer = new PropertiesConfigurer(clone.properties);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
