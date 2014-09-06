package rf.protocols.device.oregon.v2;

import rf.protocols.core.Interval;
import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2SignalListenerProperties extends AbstractProperties {
    public Interval signalLength = new Interval(200, 1200);
    public long minLongSignalLength = 700;
    public int minPreambuleSize = 32;
    public int packetSize = 80;
}
