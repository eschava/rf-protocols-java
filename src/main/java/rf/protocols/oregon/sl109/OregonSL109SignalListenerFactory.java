package rf.protocols.oregon.sl109;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109SignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, OregonSL109SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public OregonSL109SignalListenerFactory() {
        super(OregonSL109Message.NAME, new OregonSL109SignalListenerProperties());
    }

    @Override
    public OregonSL109SignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<BitPacket, OregonSL109Message> packetListener = new MessageFactoryPacketListener<BitPacket, OregonSL109Message>(new OregonSL109MessageFactory(getName()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public OregonSL109SignalListener createListener(PacketListener packetListener) {
        OregonSL109SignalListener signalListener = new OregonSL109SignalListener(packetListener);
        signalListener.setProperties(getProperties());
        return signalListener;
    }
}
