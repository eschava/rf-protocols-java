package rf.protocols.external.ognl;

import ognl.Ognl;
import ognl.OgnlException;
import ognl.PropertyAccessor;
import rf.protocols.core.impl.LinkedProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PropertiesConfigurer {
    Object properties;

    public PropertiesConfigurer(Object properties) {
        this.properties = properties;
    }

    public void loadFromFile(String fileName) throws IOException {
        java.util.Properties props = new LinkedProperties();
        props.load(new FileInputStream(fileName));

        loadFromProperties(props);
    }

    public void loadFromSystemProperties(String prefix) throws IOException {
        java.util.Properties props = System.getProperties();
        for (Object keyObj : props.keySet()) {
            String key = keyObj.toString();
            if (key.startsWith(prefix)) {
                String prop = key.substring(prefix.length());
                setProperty(prop, props.getProperty(key));
            }
        }
    }

    protected void loadFromProperties(java.util.Properties props) {
        for (String key : props.stringPropertyNames())
        {
            String value = props.getProperty(key);
            setProperty(key, value);
        }
    }

    public void setProperty(String name, String value) {
        try {
            Ognl.setValue(name, this, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Object getProperty(PropertyAccessor accessor, Map context, Object name) throws OgnlException {
        return accessor.getProperty(context, properties, name);
    }
}
