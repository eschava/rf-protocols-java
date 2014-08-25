package rf.protocols.core;

/**
 * Creates message from transport packet
 * Created message could be not valid (should be validated later)
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface MessageFactory<P extends Packet, M extends Message> {
    M createMessage(P packet);
}
