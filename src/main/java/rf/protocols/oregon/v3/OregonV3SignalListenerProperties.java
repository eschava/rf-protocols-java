package rf.protocols.oregon.v3;

import rf.protocols.core.Interval;
import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV3SignalListenerProperties extends AbstractSignalListenerProperties {
    public Interval signalLength = new Interval(200, 1200);
    public long minLongSignalLength = 700;
    public int minPreambuleSize = 32;
    public int packetSize = 80;
}
