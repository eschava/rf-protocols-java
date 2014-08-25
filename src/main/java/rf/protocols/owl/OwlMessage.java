package rf.protocols.owl;

import rf.protocols.core.MessageMetaData;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.AbstractMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlMessage extends AbstractMessage {
    public static final String NAME = "Owl";

    private static final MessageMetaData METADATA = new OwlMessageMetaData();

    public OwlMessage(BitPacket packet) {
        super(NAME, packet);
    }

    @Override
    public MessageMetaData getMetaData() {
        return METADATA;
    }

    @Override
    public boolean isValid() {
        return isLengthValid() && isCrcValid();
    }

    public boolean isLengthValid() {
        throw new UnsupportedOperationException();
    }

    public boolean isCrcValid() {
        return true; // TODO
    }

    public double getCurrentPower() {
        throw new UnsupportedOperationException();
    }

    public double getTotalPower() {
        throw new UnsupportedOperationException();
    }
}
