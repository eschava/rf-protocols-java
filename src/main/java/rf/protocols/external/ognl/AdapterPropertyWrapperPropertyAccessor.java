package rf.protocols.external.ognl;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AdapterPropertyWrapperPropertyAccessor implements PropertyAccessor {

    public static void install() {
        OgnlRuntime.setPropertyAccessor(AdapterPropertyWrapper.class, new AdapterPropertyWrapperPropertyAccessor());
    }

    @Override
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        AdapterPropertyWrapper adapterProperty = (AdapterPropertyWrapper) target;
        adapterProperty.addProperty(name);
        return adapterProperty;
    }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        AdapterPropertyWrapper adapterProperty = (AdapterPropertyWrapper) target;
        Adapter adapter = AdapterRegistry.getInstance().getAdapter(adapterProperty.getAdapter());
        adapter.setProperty(adapterProperty.getProperty(name), value.toString());
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
