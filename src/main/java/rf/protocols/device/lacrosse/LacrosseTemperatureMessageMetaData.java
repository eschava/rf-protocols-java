package rf.protocols.device.lacrosse;

import rf.protocols.core.MessageMetaData;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseTemperatureMessageMetaData implements MessageMetaData<LacrosseTemperatureMessage> {
    public static final String TEMPERATURE_FIELD = "Temperature";

    private static final Collection<String> FIELD_NAMES = Collections.singletonList(TEMPERATURE_FIELD);

    @Override
    public Collection<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public boolean isStringField(String fieldName) {
        return false;
    }

    @Override
    public double getNumericField(LacrosseTemperatureMessage message, String fieldName) {
        if (fieldName.equals(TEMPERATURE_FIELD))
            return message.getTemperature();
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringField(LacrosseTemperatureMessage message, String fieldName) {
        throw new UnsupportedOperationException();
    }
}
