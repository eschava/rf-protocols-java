package rf.protocols.core;

/**
 * High-level message sent/recieved by RF channel
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface Message {
    String getName();

    MessageMetaData<? extends Message> getMetaData();

    boolean isValid();

    Message clone();
}
