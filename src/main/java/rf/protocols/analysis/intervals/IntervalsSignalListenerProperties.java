package rf.protocols.analysis.intervals;

import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerProperties extends AbstractProperties {
    public int minSize = 10;

    public Interval interval0 = new Interval(400, 800, "0");
    public Interval interval1 = new Interval(800, 1200, "1");
    public Interval interval2 = new Interval(-1, -1, null);
    public Interval interval3 = new Interval(-1, -1, null);
    public Interval interval4 = new Interval(-1, -1, null);

    public String namesSeparator = null;
    public boolean useInterrupts = true;

    public boolean isObservableInterval(long l) {
        return interval0.isInside(l) ||
                interval1.isInside(l) ||
                interval2.isInside(l) ||
                interval3.isInside(l) ||
                interval4.isInside(l);
    }

    public String getIntervalName(long l) {
        if (interval0.isInside(l)) return interval0.getName(l);
        if (interval1.isInside(l)) return interval1.getName(l);
        if (interval2.isInside(l)) return interval2.getName(l);
        if (interval3.isInside(l)) return interval3.getName(l);
        if (interval4.isInside(l)) return interval4.getName(l);
        return null;
    }

    public static class Interval extends rf.protocols.core.Interval {
        private String name;
        private boolean format;

        public Interval(long min, long max, String name) {
            super(min, max);
            setName(name);
        }

        public void setName(String name) {
            this.name = name;
            format = name != null && name.contains("%d");
        }

        public String getName(long l) {
            if (format)
                return name.replace("%d", String.valueOf(l));
            else
                return name;
        }
    }
}
