package rf.protocols.analysis.breakdown;

import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BreakdownSignalListenerProperties extends AbstractProperties {
    public long period = 1000;
    public int minLength = 0;
    public int maxLength = 10000;
    public int groupCount = 10;

    public String adapter;
    public String pin;

    @Override
    public void setProperty(String name, String value) {
        if (name.startsWith("adapter.")) {
            Adapter adptr = AdapterRegistry.getInstance().getAdapter(adapter);
            adptr.setProperty(name.substring("adapter.".length()), value);
        } else {
            super.setProperty(name, value);
        }
    }
}
