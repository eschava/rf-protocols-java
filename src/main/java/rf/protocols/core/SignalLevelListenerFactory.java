package rf.protocols.core;

/**
* Factory of low-level RF signal listeners
*
* @author Eugene Schava <eschava@gmail.com>
*/
public interface SignalLevelListenerFactory extends SignalListenerFactory<SignalLevelListenerFactory> {
    SignalLevelListener createListener(MessageListener messageListener);
}
