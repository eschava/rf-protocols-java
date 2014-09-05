package rf.protocols.registry.interfaces;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;

/**
 * Factory of low-level RF signal listeners
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalLengthListenerFactory extends SignalListenerFactory<SignalLengthListenerFactory> {
    SignalLengthListener createListener(MessageListener messageListener);
    SignalLengthListener createListener(PacketListener packetListener);
}
