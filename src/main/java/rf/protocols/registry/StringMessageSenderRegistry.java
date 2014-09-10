package rf.protocols.registry;

import rf.protocols.core.Packet;
import rf.protocols.core.PacketFactory;
import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.message.StringMessage;
import rf.protocols.device.generic.intervals.IntervalsPacketFactory;
import rf.protocols.device.generic.intervals.IntervalsPacketSender;
import rf.protocols.device.remoteswitch.RemoteSwitchPacketFactory;
import rf.protocols.device.remoteswitch.RemoteSwitchPacketSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class StringMessageSenderRegistry {
    private static final StringMessageSenderRegistry INSTANCE = new StringMessageSenderRegistry();

//    private final Set<String> factoryNames = new HashSet<String>();
    private final Map<String, PacketSender> packetSenderMap = new HashMap<String, PacketSender>();
    private final Map<String, PacketFactory> packetFactoryMap = new HashMap<String, PacketFactory>();

    public static StringMessageSenderRegistry getInstance() {
        return INSTANCE;
    }

    private StringMessageSenderRegistry() {
        // register all known factories
        RemoteSwitchPacketSender switchPacketSender = new RemoteSwitchPacketSender();
        registerSender(switchPacketSender.getName(), switchPacketSender);
        registerPacketFactory(switchPacketSender.getName(), new RemoteSwitchPacketFactory());

        IntervalsPacketSender intervalsPacketSender = new IntervalsPacketSender();
        registerSender(intervalsPacketSender.getName(), intervalsPacketSender);
        registerPacketFactory(intervalsPacketSender.getName(), new IntervalsPacketFactory());
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
        }
    }

    public boolean cloneProtocol(String oldName, String newName) {
        if (packetSenderMap.containsKey(oldName)) {
            PacketSender packetSender = packetSenderMap.get(oldName);
            packetSender = packetSender.clone(newName);
            packetSenderMap.put(newName, packetSender);

            PacketFactory packetFactory = packetFactoryMap.get(oldName);
//            packetFactory = packetFactory.clone(newName);
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
