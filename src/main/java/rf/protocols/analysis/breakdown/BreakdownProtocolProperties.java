package rf.protocols.analysis.breakdown;

import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BreakdownProtocolProperties extends AbstractProperties {
    public long period = 1000;
    public int minLength = 0;
    public int maxLength = 10000;
    public int groupCount = 10;

    public String adapter;
    public String pin;
}
