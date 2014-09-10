package rf.protocols.device.generic.intervals;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.core.message.StringMessage;
import rf.protocols.core.message.StringMessageFactory;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.registry.interfaces.ConceptSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, IntervalsSignalListenerProperties>
        implements SignalLengthListenerFactory, ConceptSignalListenerFactory {

    public IntervalsSignalListenerFactory() {
        super("Intervals", new IntervalsSignalListenerProperties());
    }

    @Override
    public IntervalsSignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<IntervalsPacket, StringMessage> packetListener =
                new MessageFactoryPacketListener<IntervalsPacket, StringMessage>(
                        new StringMessageFactory<IntervalsPacket>(getProtocol()),
                        messageListener);
        return createListener(packetListener);
    }

    @Override
    public IntervalsSignalListener createListener(PacketListener packetListener) {
        IntervalsSignalListener signalListener = new IntervalsSignalListener(packetListener);
        signalListener.setProperties(getProperties());
        return signalListener;
    }
}
