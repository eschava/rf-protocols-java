package rf.protocols.external.paho;

import org.junit.Test;
import org.stringtemplate.v4.ST;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMessageListenerTest {
    @Test
    public void testTopicTemplate() throws Exception {
        assertEquals("/rf/Temperature", format(new ST("/rf/<field>"), new TestMessage(), "Temperature"));
        assertEquals("/rf/ch1/Temperature", format(new ST("/rf/ch<message.channel>/<field>"), new TestMessage(), "Temperature"));
        assertEquals("/rf/ch1/Temperature", format(new ST("/rf<if(message.device)>/dev<message.device><endif>/ch<message.channel>/<field>"), new TestMessage(), "Temperature"));
        assertEquals("/rf/ch1/Temperature", format(new ST("/rf<if(message.null)>/dev<message.device><endif>/ch<message.channel>/<field>"), new TestMessage(), "Temperature"));
        assertEquals("/rf/dev/ch1/Temperature", format(new ST("/rf<if(message.empty)>/dev<message.device><endif>/ch<message.channel>/<field>"), new TestMessage(), "Temperature"));
    }

    private String format(ST template, Object message, String field) {
        template.add("message", message);
        template.add("field", field);
        return template.render();
    }

    private static class TestMessage {
        public int getChannel() {
            return 1;
        }

        public String getNull() {
            return null;
        }

        public String getEmpty() {
            return "";
        }
    }
}
