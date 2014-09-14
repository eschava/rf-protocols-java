package rf.protocols.external.ognl;

import ognl.NoSuchPropertyException;
import ognl.ObjectPropertyAccessor;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OgnlObjectPropertyAccessor extends ObjectPropertyAccessor {

    public static void install() {
        OgnlRuntime.setPropertyAccessor(Object.class, new OgnlObjectPropertyAccessor());
    }

    @Override
    public Object getProperty(Map context, Object target, Object oname) throws OgnlException {
        try {
            return super.getProperty(context, target, oname);
        } catch (NoSuchPropertyException e) {
            return null;
        }
    }
}
