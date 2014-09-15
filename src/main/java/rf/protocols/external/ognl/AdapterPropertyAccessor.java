package rf.protocols.external.ognl;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import rf.protocols.external.Adapter;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AdapterPropertyAccessor implements PropertyAccessor {

    public static void install() {
        OgnlRuntime.setPropertyAccessor(Adapter.class, new AdapterPropertyAccessor());
    }

    @Override
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        ((Adapter)target).setProperty(name.toString(), value.toString());
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
