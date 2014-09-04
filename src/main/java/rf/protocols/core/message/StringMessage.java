package rf.protocols.core.message;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class StringMessage implements Message, Cloneable {
    private static final MessageMetaData<? extends Message> METADATA = new StringMessageMetaData();

    private final String name;
    private final String message;

    public StringMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MessageMetaData<? extends Message> getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getValue() {
        return message;
    }

    @Override
    public Message clone() {
        try {
            return (Message) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "StringMessage{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    private static class StringMessageMetaData implements MessageMetaData<StringMessage> {
        private static final String VALUE = "Value";

        @Override
        public Collection<String> getFieldNames() {
            return Collections.singletonList(VALUE);
        }

        @Override
        public boolean isStringField(String fieldName) {
            return true;
        }

        @Override
        public double getNumericField(StringMessage message, String fieldName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStringField(StringMessage message, String fieldName) {
            if (fieldName.equals(VALUE))
                return message.getValue();
            throw new UnsupportedOperationException();
        }
    }
}
