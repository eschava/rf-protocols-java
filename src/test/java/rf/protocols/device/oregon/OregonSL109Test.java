package rf.protocols.device.oregon;

import org.junit.Test;
import rf.protocols.core.MessageListener;
import rf.protocols.device.oregon.sl109.OregonSL109Message;
import rf.protocols.device.oregon.sl109.OregonSL109SignalListener;
import rf.protocols.device.oregon.sl109.OregonSL109SignalListenerFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonSL109Test {
    private static final String DATA = "0_1_$_0_0_0_1_1_0_0_0_1_1_0_0_1_0_0_0_0_1_0_0_0_1_1_0_1_0_0_1_0_0_1_1_0_1_1_1_0_1_$_0_0_";
    private static final String DATA2 = "0_$_1_1_0_1_1_0_0_0_1_1_0_1_0_0_0_0_0_1_0_0_0_0_0_1_0_1_0_1_0_0_1_1_0_1_1_1_0_1_$_1_1_0_1";

    @Test
    public void test() throws Exception {
        OregonSL109Message message = getMessage(DATA);

        assertEquals(2, message.getChannelId());
        assertEquals(32d, message.getHumidity(), 0d);
        assertEquals(28.2, message.getTemperature(), 0d);
        assertEquals(4, message.getStatus());
        assertEquals(221, message.getRollingCode());
    }

    @Test
    public void test2() throws Exception {
        OregonSL109Message message = getMessage(DATA2);

        assertEquals(2, message.getChannelId());
        assertEquals(34d, message.getHumidity(), 0d);
        assertEquals(26.1, message.getTemperature(), 0d);
        assertEquals(4, message.getStatus());
        assertEquals(221, message.getRollingCode());
    }

    private OregonSL109Message getMessage(String data) {
        final OregonSL109Message[] messages = new OregonSL109Message[1];
        MessageListener<OregonSL109Message> messageListener = new MessageListener<OregonSL109Message>() {
            @Override
            public void onMessage(OregonSL109Message message) {
                messages[0] = (OregonSL109Message) message.clone();
            }
        };
        OregonSL109SignalListenerFactory factory = new OregonSL109SignalListenerFactory();
        OregonSL109SignalListener signalLengthListener = factory.createListener(messageListener);

        for (byte b : data.getBytes())
        {
            int l = b == '$' ? 9000 : b == '1' ? 4000 : b == '0' ? 2000 : 500;
            signalLengthListener.onSignal(false, l);
        }

        return messages[0];
    }
}
