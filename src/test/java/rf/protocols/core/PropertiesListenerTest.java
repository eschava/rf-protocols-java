package rf.protocols.core;

import org.junit.Test;
import rf.protocols.core.impl.AbstractSignalListenerProperties;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PropertiesListenerTest {
    @Test
    public void test() throws Exception {
        Properties props = new Properties();
        props.setProperty("prop1", "20");
        assertEquals(20, props.prop1);
        props.setProperty("interval.max", "40");
        assertEquals(40, props.interval.getMax());
        props.setProperty("interval.delta", "15");
        assertEquals(45, props.interval.getMax());
        assertEquals(15, props.interval.getMin());
        props.setProperty("interval.tolerance", "0.2");
        assertEquals(36, props.interval.getMax());
        assertEquals(24, props.interval.getMin());
    }

    public static class Properties extends AbstractSignalListenerProperties {
        public int prop1 = 10;
        public Interval interval = new Interval(20, 30);
    }
}
