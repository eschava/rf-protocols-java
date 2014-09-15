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
public class ProtocolPropertyWrapperPropertyAccessor implements PropertyAccessor {

    public static void install() {
        OgnlRuntime.setPropertyAccessor(ProtocolPropertyWrapper.class, new ProtocolPropertyWrapperPropertyAccessor());
    }

    @Override
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        ProtocolPropertyWrapper protocolProperty = (ProtocolPropertyWrapper) target;
        protocolProperty.addProperty(name);
        return protocolProperty;
    }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        ProtocolPropertyWrapper protocolProperty = (ProtocolPropertyWrapper) target;
        ProtocolConfigurer.getInstance().setProtocolProperty(
                protocolProperty.getProtocol(),
                protocolProperty.getProperty(name),
                value.toString());
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
