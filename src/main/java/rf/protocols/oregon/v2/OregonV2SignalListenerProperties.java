package rf.protocols.oregon.v2;

import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2SignalListenerProperties extends AbstractSignalListenerProperties {
    public long minSignalLength = 200;
    public long maxSignalLength = 1200;
    public long minLongSignalLength = 700;
    public int minPreambuleSize = 32;
    public int packetSize = 80;
}
