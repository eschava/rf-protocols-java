package rf.protocols.device.oregon.v2.message;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2TemperatureMessageMetaData<M extends OregonV2TemperatureMessage> extends OregonV2AbstractMessageMetaData<M> {
    public static final String TEMPERATURE_FIELD = "Temperature";

    private Collection<String> fieldNames = new ArrayList<String>(super.getFieldNames());

    public OregonV2TemperatureMessageMetaData() {
        fieldNames.add(TEMPERATURE_FIELD);
    }

    @Override
    public Collection<String> getFieldNames() {
        return fieldNames;
    }

    @Override
    public double getNumericField(M message, String fieldName) {
        if (fieldName.equals(TEMPERATURE_FIELD))
            return message.getTemperature();
        return super.getNumericField(message, fieldName);
    }
}
