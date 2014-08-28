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
    private String name;
    protected P packet;

    public AbstractMessage(String name, P packet) {
        this.name = name;
        this.packet = packet;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName()).append("{").append("name='").append(name).append('\'');

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
