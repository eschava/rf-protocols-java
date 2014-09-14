package rf.protocols.device.generic.intervalsequence;

import rf.protocols.core.Properties;
import rf.protocols.core.impl.ResizeableArrayList;
import rf.protocols.device.generic.intervals.IntervalsSignalListenerProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalSequenceProtocolProperties extends IntervalsSignalListenerProperties {
    public String prefix = "";
    public List<Sequence> sequence = new ResizeableArrayList<Sequence>(new SequenceFactory());
    public String postfix = "";

    Map<String, String> createNameToSequenceMap() {
        Map<String, String> result = new HashMap<String, String>(sequence.size());
        for (Sequence seq : sequence)
            result.put(seq.name, seq.sequence);
        return result;
    }

    @Override
    public Properties clone() {
        IntervalSequenceProtocolProperties clone = (IntervalSequenceProtocolProperties) super.clone();
        // clone intervals
        clone.sequence = (List<Sequence>) ((ArrayList<Sequence>)sequence).clone();
        List<Sequence> sequenceClone = clone.sequence;
        for (int i = 0; i < sequenceClone.size(); i++) {
            Sequence seq = sequenceClone.get(i);
            sequenceClone.set(i, seq.clone());
        }
        return clone;
    }

    public static class Sequence implements Cloneable{
        public String name;
        public String sequence;

        @Override
        protected Sequence clone() {
            try {
                return (Sequence) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class SequenceFactory implements ResizeableArrayList.Factory<Sequence> {
        @Override
        public Sequence create(int index) {
            return new Sequence();
        }
    }
}
