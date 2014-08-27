package rf.protocols.pt2262;

import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugeny.Schava
 */
public class PT2262SignalListenerProperties extends AbstractSignalListenerProperties {
    public int minPacketSize = 4;

    public long minSeparatorLength = 15000;
    public long maxSeparatorLength = Long.MAX_VALUE;
    public long secondSeparatorDelta = 200;
    public double shortSignalFraction = 1d / 31;
    public double shortSignalTolerance = 0.6;
    public double longSignalFraction = 3d / 31;
    public double longSignalTolerance = 0.2;
}
