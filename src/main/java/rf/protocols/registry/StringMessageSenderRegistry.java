package rf.protocols.registry;

import rf.protocols.core.Packet;
import rf.protocols.core.PacketFactory;
import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.message.StringMessage;
import rf.protocols.remoteswitch.RemoteSwitchPacketFactory;
import rf.protocols.remoteswitch.RemoteSwitchPacketSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class StringMessageSenderRegistry {
    private static final StringMessageSenderRegistry INSTANCE = new StringMessageSenderRegistry();

    private final Set<String> factoryNames = new HashSet<String>();
    private final Map<String, PacketSender> packetSenderMap = new HashMap<String, PacketSender>();
    private final Map<String, PacketFactory> packetFactoryMap = new HashMap<String, PacketFactory>();

    public static StringMessageSenderRegistry getInstance() {
        return INSTANCE;
    }

    private StringMessageSenderRegistry() {
        // register all known factories
        RemoteSwitchPacketSender sender = new RemoteSwitchPacketSender();
        registerSender(sender.getName(), sender);
        registerPacketFactory(sender.getName(), new RemoteSwitchPacketFactory());
    }

    private void registerSender(String name, PacketSender sender) {
        packetSenderMap.put(name, sender);
    }

    private void registerPacketFactory(String name, PacketFactory<?, StringMessage> packetFactory) {
        packetFactoryMap.put(name, packetFactory);
    }

    public void sendMessage(String senderName, String message, SignalLengthSender signalSender) {
        StringMessage stringMessage = new StringMessage(senderName, message);

        PacketFactory packetFactory = packetFactoryMap.get(senderName);
        PacketSender packetSender = packetSenderMap.get(senderName);

        Packet packet = packetFactory.createPacket(stringMessage);
        packetSender.send(packet, signalSender);
    }
}
