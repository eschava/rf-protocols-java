package rf.protocols.oregon;

import org.junit.Test;
import rf.protocols.core.MessageListener;
import rf.protocols.oregon.v3.OregonV3SignalListener;
import rf.protocols.owl.OwlMessage;
import rf.protocols.owl.OwlSignalListenerFactory;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV3Test {
    private static final String OWL_DATA = "0000000000000000000000000000011100001001000000000010011000000001110000001000011100000000000000000000000010000000000001100001000000111100001111100001000000000000000000000000000000000000111110000111100001110";

//    @Test
//    public void testSignalListener() throws Exception {
//        final OregonV3Message[] messages = new OregonV3Message[1];
//        MessageListener<OregonV3Message> messageListener = new MessageListener<OregonV3Message>() {
//            @Override
//            public void onMessage(OregonV3Message message) {
//                messages[0] = (OregonV3Message) message.clone();
//            }
//        };
//        OregonV3SignalListenerFactory factory = new OregonV3SignalListenerFactory();
//        OregonV3SignalListener signalLengthListener = factory.createListener(messageListener);
//
//         // custom changes for my THN132N
//        signalLengthListener.getProperties().minPreambuleSize = 20;
//        signalLengthListener.getProperties().packetSize = 68;
//
//        for (byte b : DATA.getBytes())
//        {
//            signalLengthListener.onSignal(false, b == '1' ? 1000 : 500);
//        }
//
//        OregonV3Message message = messages[0];
////        assertEquals("ea4c", message.getDeviceType());
////        assertEquals(28.3d, message.getTemperature(), 0d);
//    }

    @Test
    public void testOwlListener() throws Exception {
        final OwlMessage[] messages = new OwlMessage[1];
        MessageListener<OwlMessage> messageListener = new MessageListener<OwlMessage>() {
            @Override
            public void onMessage(OwlMessage message) {
                messages[0] = (OwlMessage) message.clone();
            }
        };
        OwlSignalListenerFactory factory = new OwlSignalListenerFactory();
        OregonV3SignalListener signalLengthListener = factory.createListener(messageListener);

        // custom changes for my THN132N
        signalLengthListener.getProperties().minPreambuleSize = 20;
//        signalLengthListener.getProperties().packetSize = 68;

        for (byte b : OWL_DATA.getBytes())
        {
            signalLengthListener.onSignal(false, b == '1' ? 1000 : 500);
        }

        OwlMessage message = messages[0];
        System.out.println(message);
    }
}
