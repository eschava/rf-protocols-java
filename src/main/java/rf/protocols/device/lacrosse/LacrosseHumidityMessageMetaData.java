package rf.protocols.device.lacrosse;

import rf.protocols.core.MessageMetaData;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseHumidityMessageMetaData implements MessageMetaData<LacrosseHumidityMessage> {
    public static final String HUMIDITY_FIELD = "Humidity";

    private static final Collection<String> FIELD_NAMES = Collections.singletonList(HUMIDITY_FIELD);

    @Override
    public Collection<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public boolean isStringField(String fieldName) {
        return false;
    }

    @Override
    public double getNumericField(LacrosseHumidityMessage message, String fieldName) {
        if (fieldName.equals(HUMIDITY_FIELD))
            return message.getHumidity();
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringField(LacrosseHumidityMessage message, String fieldName) {
        throw new UnsupportedOperationException();
    }
}
