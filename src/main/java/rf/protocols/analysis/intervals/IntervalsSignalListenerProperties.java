package rf.protocols.analysis.intervals;

import rf.protocols.core.Properties;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.core.impl.ResizeableArrayList;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsSignalListenerProperties extends AbstractProperties {
    public int minSize = 10;

    public List<NamedInterval> interval = new ResizeableArrayList<NamedInterval>(new NamedIntervalFactory());

    public String namesSeparator = null;

    public String adapter;
    public String pin;

    public boolean isObservableInterval(long l) {
        for (NamedInterval in : interval)
            if (in.isInside(l))
                return true;
        return false;
    }

    public String getIntervalName(long l, boolean level) {
        for (NamedInterval in : interval)
            if (in.isInside(l))
                return in.getName(l, level);
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

    @Override
    public Properties clone() {
        IntervalsSignalListenerProperties clone = (IntervalsSignalListenerProperties) super.clone();
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
        private boolean format;

        public NamedInterval(long min, long max, String name) {
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

    private class NamedIntervalFactory implements ResizeableArrayList.Factory<NamedInterval> {
        @Override
        public NamedInterval create(int index) {
            return new NamedInterval(-1, -1, String.valueOf(index));
        }
    }
}
