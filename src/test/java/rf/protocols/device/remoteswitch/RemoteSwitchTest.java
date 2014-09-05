package rf.protocols.device.remoteswitch;

import org.junit.Test;
import rf.protocols.core.MessageListener;
import rf.protocols.core.message.StringMessage;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchTest {
    private static final String DATA = "110010101100S1010101010101010101001010110011001100110010101100S10101010101010101010010101100110011001";

    @Test
    public void testListener() throws Exception {
        final StringMessage[] messages = new StringMessage[1];
        MessageListener<StringMessage> messageListener = new MessageListener<StringMessage>() {
            @Override
            public void onMessage(StringMessage message) {
                messages[0] = (StringMessage) message.clone();
            }
        };
        RemoteSwitchSignalListenerFactory factory = new RemoteSwitchSignalListenerFactory();
        RemoteSwitchSignalListener signalLengthListener = factory.createListener(messageListener);

        for (byte b : DATA.getBytes())
        {
            int l = b == 'S' ? 4400 : b == '1' ? 400 : 150;
            signalLengthListener.onSignal(false, l);
        }

        StringMessage message = messages[0];
        assertEquals("111110222202", message.getValue());
    }
}
