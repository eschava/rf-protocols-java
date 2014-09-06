package rf.protocols.device.owl;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.device.oregon.v3.OregonV3SignalListener;
import rf.protocols.device.oregon.v3.OregonV3SignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlSignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, OregonV3SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public OwlSignalListenerFactory() {
        super(OwlMessage.PROTOCOL, new OregonV3SignalListenerProperties());
    }

    @Override
    public OregonV3SignalListener createListener(MessageListener messageListener) {
        PacketListener<BitPacket> packetListener = new MessageFactoryPacketListener<BitPacket, OwlMessage>(new OwlMessageFactory(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public OregonV3SignalListener createListener(PacketListener packetListener) {
        OregonV3SignalListener listener = new OregonV3SignalListener(packetListener);
        listener.setProperties(getProperties());
        return listener;
    }
}
