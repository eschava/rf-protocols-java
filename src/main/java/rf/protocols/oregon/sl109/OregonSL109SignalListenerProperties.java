package rf.protocols.oregon.sl109;

import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109SignalListenerProperties extends AbstractSignalListenerProperties {
    // TODO: move to configuration
    public long preambuleMinLength = 400;
    public long preambuleMaxLength = 600;
    public long zeroMinLength = 1600;
    public long zeroMaxLength = 2200;
    public long oneMinLength = 3800;
    public long oneMaxLength = 4200;
    public long endMinLength = 8000;
    public long endMaxLength = 10000;
}
