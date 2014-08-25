package rf.protocols.core;

/**
 * Low-level RF signal listener
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalLengthListener {
    void onSignal(boolean high, long lengthInMicros);
}
