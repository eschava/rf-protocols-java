package rf.protocols.device.generic.intervalsequence;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.impl.MessageFactoryPacketListener;
import rf.protocols.core.message.StringMessage;
import rf.protocols.device.generic.intervals.IntervalsPacket;
import rf.protocols.device.generic.intervals.IntervalsSignalListener;
import rf.protocols.registry.interfaces.AbstractSignalListenerFactory;
import rf.protocols.registry.interfaces.ConceptSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalSequenceSignalListenerFactory
        extends AbstractSignalListenerFactory<SignalLengthListenerFactory, IntervalSequenceProtocolProperties>
        implements SignalLengthListenerFactory, ConceptSignalListenerFactory {

    public IntervalSequenceSignalListenerFactory() {
        super("IntervalSequence", new IntervalSequenceProtocolProperties());
    }

    @Override
    public IntervalsSignalListener createListener(MessageListener messageListener) {
        MessageFactoryPacketListener<IntervalsPacket, StringMessage> packetListener =
                new MessageFactoryPacketListener<IntervalsPacket, StringMessage>(
                        new IntervalSequenceMessageFactory(getProtocol(), getProperties()),
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
