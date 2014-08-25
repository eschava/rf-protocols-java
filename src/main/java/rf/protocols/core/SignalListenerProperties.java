package rf.protocols.core;

/**
 * Configurable properties of low-level RF signal listener
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalListenerProperties {
    void setProperty(String name, String value);
    SignalListenerProperties clone();
}
