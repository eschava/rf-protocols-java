package rf.protocols.device.oregon.v2;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2SignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, OregonV2SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public OregonV2SignalListenerFactory() {
        super(OregonV2Message.PROTOCOL, new OregonV2SignalListenerProperties());
    }

    @Override
    public OregonV2SignalListener createListener(MessageListener messageListener) {
        PacketListener<BitPacket> packetListener = new MessageFactoryPacketListener<BitPacket, OregonV2Message>(new OregonV2MessageFactory(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public OregonV2SignalListener createListener(PacketListener packetListener) {
        OregonV2SignalListener listener = new OregonV2SignalListener(packetListener);
        listener.setProperties(getProperties());
        return listener;
    }
}
