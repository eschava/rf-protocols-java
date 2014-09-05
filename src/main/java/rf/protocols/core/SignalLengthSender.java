package rf.protocols.core;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface SignalLengthSender {
    void send(boolean high, long lengthInMicros);
}
