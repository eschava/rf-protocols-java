package rf.protocols.oregon.sl109;

import rf.protocols.core.Interval;
import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109SignalListenerProperties extends AbstractSignalListenerProperties {
    // TODO: move to configuration
    public Interval preambuleLength = new Interval(400, 600);
    public Interval zeroLength = new Interval(1600, 2200);
    public Interval oneLength = new Interval(3800, 4200);
    public Interval endLength = new Interval(8000, 10000);
    public int packetSize = 38;
}
