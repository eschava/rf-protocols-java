package rf.protocols.device.oregon.v3;

import rf.protocols.core.Interval;
import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV3SignalListenerProperties extends AbstractProperties {
    public Interval signalLength = new Interval(200, 1200);
    public long minLongSignalLength = 700;
    public int minPreambuleSize = 24;
    public int packetSize = 80;
}
