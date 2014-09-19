package rf.protocols.external.ognl;

import ognl.DefaultTypeConverter;

import java.util.Map;

/**
 * Fix for default {@link ognl.DefaultTypeConverter}
 * Made it tolerant for boolean "false" value
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class CustomTypeConverter extends DefaultTypeConverter {
    @Override
    public Object convertValue(Map context, Object value, Class toType) {
        if (toType == boolean.class && value instanceof String)
            return Boolean.valueOf((String) value);
        return super.convertValue(context, value, toType);
    }
}
