package rf.protocols.registry.interfaces;

import rf.protocols.core.MessageListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLevelListener;

/**
* Factory of low-level RF signal listeners
*
* @author Eugene Schava <eschava@gmail.com>
*/
public interface SignalLevelListenerFactory extends SignalListenerFactory<SignalLevelListenerFactory> {
    SignalLevelListener createListener(MessageListener messageListener);
    SignalLevelListener createListener(PacketListener packetListener);
}
