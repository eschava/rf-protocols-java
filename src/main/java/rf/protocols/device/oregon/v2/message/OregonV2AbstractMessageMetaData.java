package rf.protocols.device.oregon.v2.message;

import rf.protocols.core.MessageMetaData;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2AbstractMessageMetaData <M extends OregonV2AbstractMessage> implements MessageMetaData<M> {
    private static final String LOW_BATTERY_FIELD = "LowBattery";
    private static final Collection<String> FIELD_NAMES = Arrays.asList(LOW_BATTERY_FIELD);

    @Override
    public Collection<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public boolean isStringField(String fieldName) {
        return false;
    }

    @Override
    public double getNumericField(M message, String fieldName) {
        if (fieldName.equals(LOW_BATTERY_FIELD))
            return message.isBatteryLow() ? 1 : 0;
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringField(M message, String fieldName) {
        throw new UnsupportedOperationException();
    }
}
