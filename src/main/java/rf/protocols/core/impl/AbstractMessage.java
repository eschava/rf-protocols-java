package rf.protocols.core.impl;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;
import rf.protocols.core.Packet;

/**
 * Helper implementation of {@link Message} using packet for reading/writing data
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public abstract class AbstractMessage<P extends Packet> implements Message, Cloneable {
    private String protocol;
    protected P packet;

    public AbstractMessage(String protocol, P packet) {
        this.protocol = protocol;
        this.packet = packet;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName()).append("{").append("protocol='").append(protocol).append('\'');

        MessageMetaData<Message> metaData = (MessageMetaData<Message>) getMetaData();
        for (String field : metaData.getFieldNames()) {
            result.append(", ").append(field).append("=");

            if (metaData.isStringField(field))
                result.append("'").append(metaData.getStringField(this, field)).append("'");
            else
                result.append("'").append(metaData.getNumericField(this, field)).append("'");
        }
        result.append("}");

        return result.toString();
    }

    @Override
    public Message clone() {
        try {
            AbstractMessage result = (AbstractMessage) super.clone();
            result.packet = packet.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
