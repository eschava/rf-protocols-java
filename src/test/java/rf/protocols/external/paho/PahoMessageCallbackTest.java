package rf.protocols.external.paho;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMessageCallbackTest {
    @Test
    public void testTopicParse() throws Exception {
        PahoMessageCallback messageCallback = new PahoMessageCallback(null, null, "/rf/send/+/end");
        assertEquals("PT2262", messageCallback.getProtocol("/rf/send/PT2262/end"));

        messageCallback = new PahoMessageCallback(null, null, "/rf/send/+");
        assertEquals("PT2262", messageCallback.getProtocol("/rf/send/PT2262"));
    }
}
