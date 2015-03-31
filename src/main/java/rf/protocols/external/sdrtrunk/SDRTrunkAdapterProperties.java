package rf.protocols.external.sdrtrunk;

import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SDRTrunkAdapterProperties extends AbstractProperties {
    public long frequency = 433920000; // 433.92 MHz
    public float threshold = 40; // threshold for OOK detection
}
