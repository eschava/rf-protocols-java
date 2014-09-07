package rf.protocols.device.oregon.v2.message;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2TemperatureHumidityMessageMetaData extends OregonV2TemperatureMessageMetaData<OregonV2TemperatureHumidityMessage> {
    public static final String HUMIDITY_FIELD = "Humidity";

    private Collection<String> fieldNames = new ArrayList<String>(super.getFieldNames());

    public OregonV2TemperatureHumidityMessageMetaData() {
        fieldNames.add(HUMIDITY_FIELD);
    }

    @Override
    public Collection<String> getFieldNames() {
        return fieldNames;
    }

    @Override
    public boolean isStringField(String fieldName) {
        if (fieldName.equals(HUMIDITY_FIELD))
            return false;
        return super.isStringField(fieldName);
    }

    @Override
    public double getNumericField(OregonV2TemperatureHumidityMessage message, String fieldName) {
        if (fieldName.equals(HUMIDITY_FIELD))
            return message.getHumidity();
        return super.getNumericField(message, fieldName);
    }
}
