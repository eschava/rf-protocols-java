package rf.protocols.external.paho;

import org.junit.Test;
import rf.protocols.external.ognl.OgnlMessageFormatter;
import rf.protocols.external.ognl.OgnlObjectPropertyAccessor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMessageListenerTest {
    @Test
    public void testTopicTemplate() throws Exception {
        OgnlObjectPropertyAccessor.install();

        assertEquals("/rf/Temperature", format("/rf/${field}", new TestMessage(), "Temperature"));
        assertEquals("/rf/ch1/Temperature", format("/rf/ch${message.channel}/${field}", new TestMessage(), "Temperature"));
        assertEquals("/rf/ch1/Temperature", format("/rf${message.device ? \"/dev\" + message.device : \"\"}/ch${message.channel}/${field}", new TestMessage(), "Temperature"));
        assertEquals("/rf/ch1/Temperature", format("/rf${message.nil    ? \"/dev\" + message.nil    : \"\"}/ch${message.channel}/${field}", new TestMessage(), "Temperature"));
        assertEquals("/rf/dev/ch1/Temperature", format("/rf${message.empty ? \"/dev\" + message.empty    : \"\"}/ch${message.channel}/${field}", new TestMessage(), "Temperature"));
    }

    private String format(String template, Object message, String field) {
        Map<String, Object> root = new HashMap<String, Object>(2);
        root.put("message", message);
        root.put("field", field);
        return OgnlMessageFormatter.format(template, root);
    }

    private static class TestMessage {
        public int getChannel() {
            return 1;
        }

        public String getNil() {
            return null;
        }

//        public String getDevice() {
//            return null;
//        }

        public String getEmpty() {
            return "";
        }
    }
}
