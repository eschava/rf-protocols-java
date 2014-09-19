package rf.protocols.external.ognl;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import rf.protocols.registry.ProtocolConfigurer;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class ProtocolRegistryConfigurerPropertyAccessor implements PropertyAccessor {

    public static void install() {
        OgnlRuntime.setPropertyAccessor(ProtocolConfigurer.class, new ProtocolRegistryConfigurerPropertyAccessor());
    }

    @Override
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        return new ProtocolPropertyWrapper(name.toString());
    }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        String valueStr = value.toString();
        // for using artificial property like NewName=CurrentName.clone
        if (valueStr.endsWith(".clone"))
        {
            String sourceProtocol = valueStr.substring(0, valueStr.length() - ".clone".length());
            String targetProtocol = name.toString();

            ProtocolConfigurer.getInstance().cloneProtocol(sourceProtocol, targetProtocol);
        }
        else
        {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        throw new UnsupportedOperationException();
    }
}
