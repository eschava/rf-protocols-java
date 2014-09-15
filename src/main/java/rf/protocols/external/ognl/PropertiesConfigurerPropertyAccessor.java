package rf.protocols.external.ognl;

import ognl.*;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PropertiesConfigurerPropertyAccessor implements PropertyAccessor {
    private PropertyAccessor delegate = new ObjectPropertyAccessor();

    public static void install() {
        OgnlRuntime.setPropertyAccessor(PropertiesConfigurer.class, new PropertiesConfigurerPropertyAccessor());
    }

    @Override
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        return ((PropertiesConfigurer)target).getProperty(delegate, context, name);
    }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        delegate.setProperty(context, ((PropertiesConfigurer)target).properties, name, value);
    }

    @Override
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        return delegate.getSourceAccessor(context, ((PropertiesConfigurer)target).properties, index);
    }

    @Override
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        return delegate.getSourceSetter(context, ((PropertiesConfigurer)target).properties, index);
    }
}
