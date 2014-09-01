package rf.protocols.owl;

import rf.protocols.core.MessageMetaData;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OwlMessageMetaData implements MessageMetaData<OwlMessage> {
    public static final String CURRENT_POWER_FIELD = "CurrentPower";
    public static final String TOTAL_POWER_FIELD = "TotalPower";

    private static final Collection<String> FIELD_NAMES = Arrays.asList(CURRENT_POWER_FIELD, TOTAL_POWER_FIELD);

    @Override
    public Collection<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @Override
    public boolean isStringField(String fieldName) {
        return false;
    }

    @Override
    public double getNumericField(OwlMessage message, String fieldName) {
        if (fieldName.equals(CURRENT_POWER_FIELD))
            return message.getCurrentPower();
        else if (fieldName.equals(TOTAL_POWER_FIELD))
            return message.getTotalPower();
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringField(OwlMessage message, String fieldName) {
        throw new UnsupportedOperationException();
    }
}
