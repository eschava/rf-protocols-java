package rf.protocols.external.paho;

import ognl.OgnlException;
import ognl.PropertyAccessor;
import rf.protocols.external.ognl.AdapterPropertyWrapper;
import rf.protocols.external.ognl.PropertiesWithAdapterConfigurer;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class MqttPropertiesConfigurer extends PropertiesWithAdapterConfigurer {
    private String sendAdapter;
    private String receiveAdapter;

    public MqttPropertiesConfigurer(Object properties) {
        super(properties);
    }

    @Override
    public void setProperty(String name, String value) {
        if (name.equals("sendAdapter"))
            sendAdapter = value;
        else if (name.equals("receiveAdapter"))
            receiveAdapter = value;

        super.setProperty(name, value);
    }

    @Override
    protected Object getProperty(PropertyAccessor accessor, Map context, Object name) throws OgnlException {
        if (name.equals("sendAdapter"))
            return new AdapterPropertyWrapper(sendAdapter);
        if (name.equals("receiveAdapter"))
            return new AdapterPropertyWrapper(receiveAdapter);

        return super.getProperty(accessor, context, name);
    }
}
