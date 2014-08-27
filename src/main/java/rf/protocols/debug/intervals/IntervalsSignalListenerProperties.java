package rf.protocols.debug.intervals;

import rf.protocols.core.impl.AbstractSignalListenerProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerProperties extends AbstractSignalListenerProperties {
    public int minSize = 10;

    public long interval0Min = 400;
    public long interval0Max = 800;
    public String interval0Name = "0";

    public long interval1Min = 800;
    public long interval1Max = 1200;
    public String interval1Name = "1";

    public long interval2Min = -1;
    public long interval2Max = -1;
    public String interval2Name = "2";

    public long interval3Min = -1;
    public long interval3Max = -1;
    public String interval3Name = "3";

    public long interval4Min = -1;
    public long interval4Max = -1;
    public String interval4Name = "4";

    public String namesSeparator = null;
}
