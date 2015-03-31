package rf.protocols.device.ambient.ft005th;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientFT005THSignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, AmbientFT005THSignalListenerProperties>
        implements SignalLengthListenerFactory {

    public AmbientFT005THSignalListenerFactory() {
        super(AmbientFT005THMessage.PROTOCOL, new AmbientFT005THSignalListenerProperties());
    }

    @Override
    public AmbientFT005THSignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<BitPacket, AmbientFT005THMessage> packetListener = new MessageFactoryPacketListener<BitPacket, AmbientFT005THMessage>(new AmbientFT005THMessageFactory(getProtocol()), messageListener);
        return createListener(packetListener);
    }

    @Override
    public AmbientFT005THSignalListener createListener(PacketListener packetListener) {
        AmbientFT005THSignalListener signalListener = new AmbientFT005THSignalListener(packetListener);
        signalListener.setProperties(getProperties());
        return signalListener;
    }
}
