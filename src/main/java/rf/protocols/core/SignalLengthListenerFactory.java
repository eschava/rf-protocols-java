package rf.protocols.core;

/**
 * Factory of low-level RF signal listeners
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalLengthListenerFactory extends SignalListenerFactory<SignalLengthListenerFactory> {
    SignalLengthListener createListener(MessageListener messageListener);
}
