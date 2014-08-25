package rf.protocols.core;

/**
 * Listener for high-level messages
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface MessageListener<M extends Message> {
    void onMessage(M message);
}
