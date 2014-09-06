package rf.protocols.device.pt2262;

import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PT2262SignalListenerProperties extends AbstractProperties {
    public int minPacketSize = 4;

    public long minSeparatorLength = 5000;
    public long maxSeparatorLength = Long.MAX_VALUE;
    public long secondSeparatorDelta = 1000;
    public double shortSignalFraction = 1d / 31;
    public double shortSignalTolerance = 0.6;
    public double longSignalFraction = 3d / 31;
    public double longSignalTolerance = 0.2;
}
