package rf.protocols.device.generic.intervals;

import rf.protocols.core.Interval;
import rf.protocols.core.Properties;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.core.impl.ResizeableArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerProperties extends AbstractProperties {
    public boolean separatorIsHigh = false;

    public Interval packetSize = new Interval(20, 2000);
    public Interval separatorInterval = new Interval(10000, 12000);
    public boolean separatorPresent = true;
    public List<NamedInterval> interval = new ResizeableArrayList<NamedInterval>(new NamedIntervalFactory());

    // sender properties
    public int repeats = 1;

    public boolean isSeparator(long l, boolean high) {
        return separatorInterval.isInside(l) && high == separatorIsHigh;
    }

    public boolean isCorrectSize(int size) {
        return packetSize.isInside(size);
    }

    public String getIntervalName(long l) {
        for (NamedInterval in : interval)
            if (in.isInside(l))
                return in.getName();
        return null;
    }

    public Map<String, Long> getIntervalLengthsMap() {
        Map<String, Long> map = new HashMap<String, Long>(interval.size());
        for (NamedInterval in : interval)
            map.put(in.getName(), in.getMed());
        return map;
    }

    @Override
    public Properties clone() {
        IntervalsSignalListenerProperties clone = (IntervalsSignalListenerProperties) super.clone();
        clone.packetSize = packetSize.clone();
        clone.separatorInterval = separatorInterval.clone();
        // clone intervals
        clone.interval = (List<NamedInterval>) ((ArrayList<NamedInterval>)interval).clone();
        List<NamedInterval> intervalClone = clone.interval;
        for (int i = 0; i < intervalClone.size(); i++) {
            NamedInterval interval = intervalClone.get(i);
            intervalClone.set(i, (NamedInterval) interval.clone());
        }
        return clone;
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

    private class NamedIntervalFactory implements ResizeableArrayList.Factory<NamedInterval> {
        @Override
        public NamedInterval create(int index) {
            return new NamedInterval(-1, -1, String.valueOf(index));
        }
    }
}
