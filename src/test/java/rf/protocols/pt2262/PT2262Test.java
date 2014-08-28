package rf.protocols.pt2262;

import org.junit.Test;
import rf.protocols.core.MessageListener;
import rf.protocols.core.message.CommandMessage;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PT2262Test {

    private static final int[] DATA = new int[] {
            13485,473,1320,520,1366,486,1429,1415,489,466,1361,516,1361,506,1367,485,1407,490,1348,1459,474,472,1381,510,1365,495,1369,1470,448,498,1337,1464,474,1450,421,1494,443,480,1420,482,1330,519,1392,466,1368,520,1359,520,1359,552,14452,410,1321,507,1397,464,1452
    };

    @Test
    public void test() throws Exception {
        final CommandMessage[] messages = new CommandMessage[1];
        MessageListener<CommandMessage> messageListener = new MessageListener<CommandMessage>() {
            @Override
            public void onMessage(CommandMessage message) {
                messages[0] = (CommandMessage) message.clone();
            }
        };
        PT2262SignalListenerFactory factory = new PT2262SignalListenerFactory();
        PT2262SignalListener signalListener = factory.createListener(messageListener);

        for (int b : DATA)
        {
            signalListener.onSignal(false, b);
        }

        CommandMessage message = messages[0];
        assertEquals("f75dfc", message.getCommand());
    }
}
