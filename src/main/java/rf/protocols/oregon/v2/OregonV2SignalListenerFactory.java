package rf.protocols.oregon.v2;

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
public class OregonV2SignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, OregonV2SignalListenerProperties>
        implements SignalLengthListenerFactory {

    public OregonV2SignalListenerFactory() {
        super(OregonV2Message.NAME, new OregonV2SignalListenerProperties());
    }

    @Override
    public SignalLengthListener createListener(MessageListener messageListener) {
        PacketListener<BitPacket> packetListener = new MessageFactoryPacketListener<BitPacket, OregonV2Message>(new OregonV2MessageFactory(getName()), messageListener);
        OregonV2SignalListener listener = new OregonV2SignalListener(packetListener);
        listener.setProperties(getProperties());
        return listener;
    }
}
