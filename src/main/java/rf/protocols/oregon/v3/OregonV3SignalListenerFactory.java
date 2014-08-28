package rf.protocols.oregon.v3;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.SignalLengthListenerFactory;
import rf.protocols.core.impl.AbstractSignalListenerFactory;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV3SignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, OregonV3SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public OregonV3SignalListenerFactory() {
        super(OregonV3Message.NAME, new OregonV3SignalListenerProperties());
    }

    @Override
    public SignalLengthListener createListener(MessageListener messageListener) {
        PacketListener<BitPacket> packetListener = new MessageFactoryPacketListener<BitPacket, OregonV3Message>(new OregonV3MessageFactory(getName()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public SignalLengthListener createListener(PacketListener packetListener) {
        OregonV3SignalListener listener = new OregonV3SignalListener(packetListener);
        listener.setProperties(getProperties());
        return listener;
    }
}
