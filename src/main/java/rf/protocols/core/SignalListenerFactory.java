package rf.protocols.core;

/**
 * Factory of low-level RF signal listeners (base interface)
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalListenerFactory<Factory extends SignalListenerFactory> {
    String getName();

    void setProperty(String property, String value);

    Factory clone(String newName);
}
