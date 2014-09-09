package rf.protocols.analysis.intervals;

import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerProperties extends AbstractProperties {
    public int minSize = 10;

    public Interval interval0 = new Interval(400, 800, "0");
    public Interval interval1 = new Interval(800, 1200, "1");
    public Interval interval2 = new Interval(-1, -1, "2");
    public Interval interval3 = new Interval(-1, -1, "3");
    public Interval interval4 = new Interval(-1, -1, "4");

    public String namesSeparator = null;

    public String adapter;
    public String pin;

    public boolean isObservableInterval(long l) {
        return interval0.isInside(l) ||
                interval1.isInside(l) ||
                interval2.isInside(l) ||
                interval3.isInside(l) ||
                interval4.isInside(l);
    }

    public String getIntervalName(long l, boolean level) {
        if (interval0.isInside(l)) return interval0.getName(l, level);
        if (interval1.isInside(l)) return interval1.getName(l, level);
        if (interval2.isInside(l)) return interval2.getName(l, level);
        if (interval3.isInside(l)) return interval3.getName(l, level);
        if (interval4.isInside(l)) return interval4.getName(l, level);
        return null;
    }

    @Override
    public void setProperty(String name, String value) {
        if (name.startsWith("adapter.")) {
            Adapter adptr = AdapterRegistry.getInstance().getAdapter(adapter);
            adptr.setProperty(name.substring("adapter.".length()), value);
        } else {
            super.setProperty(name, value);
        }
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
            format = name != null && (name.contains("%d") || name.contains("%l"));
        }

        public String getName(long l, boolean level) {
            if (format) {
                String result = name.replace("%d", String.valueOf(l));
                result = result.replace("%l", (level ? "^" : "v"));
                return result;
            }
            else
                return name;
        }
    }
}
