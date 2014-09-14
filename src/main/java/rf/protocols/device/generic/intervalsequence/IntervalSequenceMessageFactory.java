package rf.protocols.device.generic.intervalsequence;

import rf.protocols.core.MessageFactory;
import rf.protocols.core.message.StringMessage;
import rf.protocols.device.generic.intervals.IntervalsPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalSequenceMessageFactory implements MessageFactory<IntervalsPacket, StringMessage> {
    private String name;
    private IntervalSequenceProtocolProperties properties;
    private Node root;

    public IntervalSequenceMessageFactory(String name, IntervalSequenceProtocolProperties properties) {
        this.name = name;
        this.properties = properties;

        // build parse tree
        root = new Node();
        for (IntervalSequenceProtocolProperties.Sequence sequence : properties.sequence) {
            Node node = root;
            for (char c : sequence.sequence.toCharArray()) {
                Node child = node.children.get(c);
                if (child == null) node.children.put(c, child = new Node());
                node = child;
             }
            node.value = sequence.name;
        }
    }

    @Override
    public StringMessage createMessage(IntervalsPacket intervalsPacket) {
        String packet = intervalsPacket.toString();
        if (!packet.startsWith(properties.prefix) || !packet.endsWith(properties.postfix))
            return null;
        // remove prefix and postfix from packet
        packet = packet.substring(properties.prefix.length(), packet.length() - properties.postfix.length());

        // parse packet using prepared tree
        StringBuilder message = new StringBuilder();
        Node node = root;
        for (char c : packet.toCharArray()) {
            Node child = node.children.get(c);
            if (child == null) {
                String value = node.value;
                if (value == null) // stopped on non-leaf node
                    return null;
                message.append(value);
                child = root.children.get(c);
                if (child == null)
                    return null;
            }
            node = child;
        }

        String value = node.value;
        if (value == null) // stopped on non-leaf node
            return null;
        message.append(value);

        return new StringMessage(name, message.toString());
    }

    private static class Node {
        private String value = null;
        private Map<Character, Node> children = new HashMap<Character, Node>();
    }
}
