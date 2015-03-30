package rf.protocols.device.lacrosse;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseSignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, LacrosseSignalListenerProperties>
        implements SignalLengthListenerFactory {

    public LacrosseSignalListenerFactory() {
        super(LacrosseAbstractMessage.PROTOCOL, new LacrosseSignalListenerProperties());
    }

    @Override
    public LacrosseSignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<BitPacket, LacrosseAbstractMessage> packetListener = new MessageFactoryPacketListener<BitPacket, LacrosseAbstractMessage>(new LacrosseMessageFactory(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public LacrosseSignalListener createListener(PacketListener packetListener) {
        LacrosseSignalListener signalListener = new LacrosseSignalListener(packetListener);
        signalListener.setProperties(getProperties());
        return signalListener;
    }
}
