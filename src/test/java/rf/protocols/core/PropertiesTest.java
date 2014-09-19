package rf.protocols.core;

import org.junit.Test;
import rf.protocols.core.impl.*;
import rf.protocols.external.Adapter;
import rf.protocols.external.ognl.*;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.StringMessageSenderRegistry;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PropertiesTest {
    @Test
    public void test() throws Exception {
        AdapterRegistry.getInstance().registerAdapter("test", TestAdapter.class);

        Properties props = new Properties();
        PropertiesConfigurer propertiesConfigurer = new PropertiesWithAdapterConfigurer(props);
        propertiesConfigurer.setProperty("prop1", "20");
        assertEquals(20, props.prop1);
        propertiesConfigurer.setProperty("interval.max", "40");
        assertEquals(40, props.interval.getMax());
        propertiesConfigurer.setProperty("interval.delta", "15");
        assertEquals(45, props.interval.getMax());
        assertEquals(15, props.interval.getMin());
        propertiesConfigurer.setProperty("interval.tolerance", "0.2");
        assertEquals(36, props.interval.getMax());
        assertEquals(24, props.interval.getMin());

        propertiesConfigurer.setProperty("ar[0].value", "10");
        assertEquals(10, props.ar.get(0).value);

        // test adapter
        propertiesConfigurer.setProperty("adapter", "test");
        propertiesConfigurer.setProperty("adapter.param", "test2");
        propertiesConfigurer.setProperty("adapter.bool", "false");
        assertEquals("test2", TestAdapter.props.param);
        assertEquals(false, TestAdapter.props.bool);

        // test protocol
        propertiesConfigurer.setProperty("protocol.RemoteSwitch.packetSize", "102"); // no exception
        propertiesConfigurer.setProperty("protocol.OregonSL109.zeroLength.med", "100"); // no exception

        // test protocol clone
        propertiesConfigurer.setProperty("protocol.RemoteSwitch.clone", "rs2");
        StringMessageSenderRegistry.getInstance().sendMessage("rs2", "10", new TestSender()); // no exception
    }

    public static class Properties extends AbstractProperties {
        public int prop1 = 10;
        public Interval interval = new Interval(20, 30);
        public List<ArrayProperty> ar = new ResizeableArrayList<ArrayProperty>(1, new ArrayPropertyFactory());
        public String adapter;

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

    public static class TestAdapter implements Adapter {
        public static Props props = new Props();
        private final PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(props);

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setProperty(String name, String value) {
            propertiesConfigurer.setProperty(name, value);
        }

        @Override
        public void addListener(String pin, SignalLengthListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addListener(String pin, SignalLevelListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SignalLengthSender getSignalSender(String pin) {
            throw new UnsupportedOperationException();
        }

        public static class Props
        {
            public String param;
            public boolean bool = false;
        }
    }

    private class TestSender implements SignalLengthSender {
        @Override
        public void send(boolean high, long lengthInMicros) {
        }
    }
}
