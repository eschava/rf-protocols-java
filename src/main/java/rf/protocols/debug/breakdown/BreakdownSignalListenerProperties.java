package rf.protocols.debug.breakdown;

import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BreakdownSignalListenerProperties extends AbstractSignalListenerProperties {
    public long period = 1000;
    public int minLength = 0;
    public int maxLength = 10000;
    public int groupCount = 10;
}
