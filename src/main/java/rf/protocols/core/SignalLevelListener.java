package rf.protocols.core;

/**
 * Low-level RF signal listener
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalLevelListener extends SignalListener {
    void onSignal(boolean high);
}
