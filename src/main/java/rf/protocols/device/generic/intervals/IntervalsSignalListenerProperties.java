package rf.protocols.device.generic.intervals;

import rf.protocols.core.Interval;
import rf.protocols.core.impl.AbstractProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerProperties extends AbstractProperties {
    public int minSize = 10;
    public boolean separatorIsHigh = false;

    public Interval packetSize = new Interval(20, 2000);
    public Interval separatorInterval = new Interval(10000, 12000);
    public NamedInterval interval0 = new NamedInterval(400, 800, "0");
    public NamedInterval interval1 = new NamedInterval(800, 1200, "1");
    public NamedInterval interval2 = new NamedInterval(-1, -1, "2");
    public NamedInterval interval3 = new NamedInterval(-1, -1, "3");
    public NamedInterval interval4 = new NamedInterval(-1, -1, "4");

    // sender properties
    public int repeats = 8;

    public boolean isSeparator(long l, boolean high) {
        return separatorInterval.isInside(l) && high == separatorIsHigh;
    }

    public boolean isCorrectSize(int size) {
        return packetSize.isInside(size);
    }

    public String getIntervalName(long l) {
        if (interval0.isInside(l)) return interval0.getName();
        if (interval1.isInside(l)) return interval1.getName();
        if (interval2.isInside(l)) return interval2.getName();
        if (interval3.isInside(l)) return interval3.getName();
        if (interval4.isInside(l)) return interval4.getName();
        return null;
    }

    public Map<String, Long> getIntervalLengthsMap() {
        Map<String, Long> map = new HashMap<String, Long>();
        if (interval0.getMax() > 0) map.put(interval0.getName(), interval0.getMed());
        if (interval1.getMax() > 0) map.put(interval1.getName(), interval1.getMed());
        if (interval2.getMax() > 0) map.put(interval2.getName(), interval2.getMed());
        if (interval3.getMax() > 0) map.put(interval3.getName(), interval3.getMed());
        if (interval4.getMax() > 0) map.put(interval4.getName(), interval4.getMed());
        return map;
    }

    public static class NamedInterval extends rf.protocols.core.Interval {
        private String name;

        public NamedInterval(long min, long max, String name) {
            super(min, max);
            setName(name);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
