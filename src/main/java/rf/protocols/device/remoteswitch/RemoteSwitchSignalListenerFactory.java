package rf.protocols.device.remoteswitch;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.core.message.StringMessage;
import rf.protocols.core.message.StringMessageFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchSignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, RemoteSwitchSignalListenerProperties>
        implements SignalLengthListenerFactory {

    public RemoteSwitchSignalListenerFactory() {
        super("RemoteSwitch", new RemoteSwitchSignalListenerProperties());
    }

    @Override
    public RemoteSwitchSignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<BitPacket, StringMessage> packetListener =
                new MessageFactoryPacketListener<BitPacket, StringMessage>(new StringMessageFactory<BitPacket>(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public RemoteSwitchSignalListener createListener(PacketListener packetListener) {
        RemoteSwitchSignalListener signalListener = new RemoteSwitchSignalListener(packetListener);
        signalListener.setProperties(getProperties());
        return signalListener;
    }
}
