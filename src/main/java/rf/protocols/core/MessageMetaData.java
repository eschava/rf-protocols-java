package rf.protocols.core;

import java.util.Collection;

/**
 * Metadata of the message
 * Represents all data could be get from message in runtime
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface MessageMetaData<M extends Message> {
    Collection<String> getFieldNames();
    boolean isStringField(String fieldName);
    double getNumericField(M message, String fieldName);
    String getStringField(M message, String fieldName);
}
