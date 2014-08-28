package rf.protocols.core.message;

import rf.protocols.core.Message;
import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.AbstractMessage;
import rf.protocols.core.impl.BitPacket;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class CommandMessage extends AbstractMessage<BitPacket> {
    private static final MessageMetaData<? extends Message> METADATA = new CommandMessageMetaData();

    public CommandMessage(String name, BitPacket packet) {
        super(name, packet);
    }

    @Override
    public MessageMetaData<? extends Message> getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public String getCommand() {
        StringBuilder command = new StringBuilder();
        int bit = 0;
        while (bit < packet.getSize()) {
            int endBit = bit + Math.min(8, packet.getSize() - bit);
            command.append(Integer.toHexString(packet.getInt(endBit - 1, bit)));
            bit += 8;
        }
        return command.toString();
    }

    private static class CommandMessageMetaData implements MessageMetaData<CommandMessage> {
        private static final String COMMAND = "Command";

        @Override
        public Collection<String> getFieldNames() {
            return Collections.singletonList(COMMAND);
        }

        @Override
        public boolean isStringField(String fieldName) {
            return true;
        }

        @Override
        public double getNumericField(CommandMessage message, String fieldName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStringField(CommandMessage message, String fieldName) {
            if (fieldName.equals(COMMAND))
                return message.getCommand();
            throw new UnsupportedOperationException();
        }
    }
}
