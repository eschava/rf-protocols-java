package rf.protocols.registry;

import rf.protocols.core.Packet;
import rf.protocols.core.PacketFactory;
import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.message.StringMessage;
import rf.protocols.device.generic.intervals.IntervalsPacketFactory;
import rf.protocols.device.generic.intervals.IntervalsPacketSender;
import rf.protocols.device.generic.intervalsequence.IntervalSequencePacketFactory;
import rf.protocols.device.generic.intervalsequence.IntervalSequenceProtocolProperties;
import rf.protocols.device.generic.lengths.LengthsPacketFactory;
import rf.protocols.device.generic.lengths.LengthsPacketSender;
import rf.protocols.device.remoteswitch.RemoteSwitchPacketFactory;
import rf.protocols.device.remoteswitch.RemoteSwitchPacketSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class StringMessageSenderRegistry {
    public static final String REMOTE_SWITCH_PROTOCOL = "RemoteSwitch";
    public static final String INTERVALS_PROTOCOL = "Intervals";
    public static final String INTERVAL_SEQUENCE_PROTOCOL = "IntervalSequence";
    public static final String LENGTHS_PROTOCOL = "Lengths";

    private static final StringMessageSenderRegistry INSTANCE = new StringMessageSenderRegistry();

//    private final Set<String> factoryNames = new HashSet<String>();
    private final Map<String, PacketSender> packetSenderMap = new HashMap<String, PacketSender>();
    private final Map<String, PacketFactory> packetFactoryMap = new HashMap<String, PacketFactory>();

    public static StringMessageSenderRegistry getInstance() {
        return INSTANCE;
    }

    private StringMessageSenderRegistry() {
        // register all known factories
        registerSender(REMOTE_SWITCH_PROTOCOL, new RemoteSwitchPacketSender());
        registerPacketFactory(REMOTE_SWITCH_PROTOCOL, new RemoteSwitchPacketFactory());

        registerSender(INTERVALS_PROTOCOL, new IntervalsPacketSender());
        registerPacketFactory(INTERVALS_PROTOCOL, new IntervalsPacketFactory());

        IntervalsPacketSender intervalSequencePacketSender = new IntervalsPacketSender();
        intervalSequencePacketSender.setProperties(new IntervalSequenceProtocolProperties());
        registerSender(INTERVAL_SEQUENCE_PROTOCOL, intervalSequencePacketSender);
        registerPacketFactory(INTERVAL_SEQUENCE_PROTOCOL, new IntervalSequencePacketFactory());

        registerSender(LENGTHS_PROTOCOL, new LengthsPacketSender());
        registerPacketFactory(LENGTHS_PROTOCOL, new LengthsPacketFactory());
    }

    private void registerSender(String name, PacketSender sender) {
        packetSenderMap.put(name, sender);
    }

    private void registerPacketFactory(String name, PacketFactory<?, StringMessage> packetFactory) {
        packetFactoryMap.put(name, packetFactory);
    }

    public void setProtocolProperty(String protocol, String property, String value) {
        PacketSender packetSender = packetSenderMap.get(protocol);
        if (packetSender != null) {
            packetSender.setProperty(property, value);
            packetFactoryMap.get(protocol).setProperty(property, value);
        }
    }

    public boolean cloneProtocol(String oldName, String newName) {
        if (packetSenderMap.containsKey(oldName)) {
            PacketSender packetSender = packetSenderMap.get(oldName);
            packetSender = packetSender.clone(newName);
            packetSenderMap.put(newName, packetSender);

            PacketFactory packetFactory = packetFactoryMap.get(oldName);
            packetFactory = packetFactory.clone(newName);
            packetFactoryMap.put(newName, packetFactory);

            return true;
        }

        return false;
    }

    public void sendMessage(String senderName, String message, SignalLengthSender signalSender) {
        StringMessage stringMessage = new StringMessage(senderName, message);

        PacketFactory packetFactory = packetFactoryMap.get(senderName);
        PacketSender packetSender = packetSenderMap.get(senderName);

        Packet packet = packetFactory.createPacket(stringMessage);
        packetSender.send(packet, signalSender);
    }
}
