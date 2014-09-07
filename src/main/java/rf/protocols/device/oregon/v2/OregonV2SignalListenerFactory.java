package rf.protocols.device.oregon.v2;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.device.oregon.v2.message.OregonV2AbstractMessage;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2SignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, OregonV2SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public static final String PROTOCOL = "OregonV2";

    public OregonV2SignalListenerFactory() {
        super(PROTOCOL, new OregonV2SignalListenerProperties());
    }

    @Override
    public OregonV2SignalListener createListener(MessageListener messageListener) {
        PacketListener<BitPacket> packetListener = new MessageFactoryPacketListener<BitPacket, OregonV2AbstractMessage>(
                new OregonV2MessageFactory(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public OregonV2SignalListener createListener(PacketListener packetListener) {
        OregonV2SignalListener listener = new OregonV2SignalListener(packetListener);
        listener.setProperties(getProperties());
        return listener;
    }
}
