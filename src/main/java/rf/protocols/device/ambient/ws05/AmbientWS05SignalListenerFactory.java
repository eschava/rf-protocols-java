package rf.protocols.device.ambient.ws05;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientWS05SignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, AmbientWS05SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public AmbientWS05SignalListenerFactory() {
        super(AmbientWS05Message.PROTOCOL, new AmbientWS05SignalListenerProperties());
    }

    @Override
    public AmbientWS05SignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<BitPacket, AmbientWS05Message> packetListener = new MessageFactoryPacketListener<BitPacket, AmbientWS05Message>(new AmbientWS05MessageFactory(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public AmbientWS05SignalListener createListener(PacketListener packetListener) {
        AmbientWS05SignalListener signalListener = new AmbientWS05SignalListener(packetListener);
        signalListener.setProperties(getProperties());
        return signalListener;
    }
}
