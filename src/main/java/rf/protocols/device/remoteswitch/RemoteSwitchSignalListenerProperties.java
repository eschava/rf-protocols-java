package rf.protocols.device.remoteswitch;

import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchSignalListenerProperties extends AbstractProperties {
    public int packetSize = 12; // in trits

    public long minSeparatorLength = 3720;
    public long maxSeparatorLength = Long.MAX_VALUE;
    public long secondSeparatorDelta = 1000;
    public double shortSignalFraction = 1d / 31;
    public double shortSignalTolerance = 0.6;
    public double longSignalFraction = 3d / 31;
    public double longSignalTolerance = 0.2;

    // sender properties
    public long separatorLength = 4464;
    public int repeats = 8;
}
