package rf.protocols.core;

import org.junit.Test;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.core.impl.ResizeableArrayList;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PropertiesTest {
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

        props.setProperty("ar[0].value", "10");
        assertEquals(10, props.ar.get(0).value);
    }

    public static class Properties extends AbstractProperties {
        public int prop1 = 10;
        public Interval interval = new Interval(20, 30);

        public List<ArrayProperty> ar = new ResizeableArrayList<ArrayProperty>(1, new ArrayPropertyFactory());

        public static class ArrayProperty {
            public int value;
        }

        private class ArrayPropertyFactory implements ResizeableArrayList.Factory<ArrayProperty> {
            @Override
            public ArrayProperty create(int index) {
                return new ArrayProperty();
            }
        }
    }
}
