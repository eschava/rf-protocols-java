package rf.protocols.device.ambient.ft005th;

import rf.protocols.core.MessageMetaData;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientFT005THMessageMetaData implements MessageMetaData<AmbientFT005THMessage> {
    public static final String TEMPERATURE_FIELD = "Temperature";
    public static final String HUMIDITY_FIELD = "Humidity";

    private static final Collection<String> FIELD_NAMES = Arrays.asList(TEMPERATURE_FIELD, HUMIDITY_FIELD);

    @Override
    public Collection<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public boolean isStringField(String fieldName) {
        return false;
    }

    @Override
    public double getNumericField(AmbientFT005THMessage message, String fieldName) {
        if (fieldName.equals(TEMPERATURE_FIELD))
            return message.getTemperature();
        else if (fieldName.equals(HUMIDITY_FIELD))
            return message.getHumidity();
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringField(AmbientFT005THMessage message, String fieldName) {
        throw new UnsupportedOperationException();
    }
}
