package rf.protocols.oregon;

import org.junit.Test;
import rf.protocols.core.MessageListener;
import rf.protocols.oregon.v2.OregonV2Message;
import rf.protocols.oregon.v2.OregonV2SignalListener;
import rf.protocols.oregon.v2.OregonV2SignalListenerFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2Test {
    private static final long[] DATA = {949,1020,929,969,973,1008,952,971,972,1042,913,1006,939,1036,920,974,973,1002,954,990,972,1035,923,998,942,1009,946,975,973,1008,952,510,459,1031,467,455,1001,495,456,972,523,458,1005,496,466,967,479,515,973,982,943,1006,971,494,462,969,1060,907,481,513,978,948,972,518,487,951,973,1003,515,436,979,516,451,1015,940,1004,986,971,910,1034,919,1039,484,486,968,502,455,1039,916,975,973,1032,925,978,483,547,922,970,977,1000,957,516,456,998,957,1009,478,484,955,514,425,1064,923,1005,490,510,921,517,463,988,940,999,541,436,970,975,1001,496,455,975,1010,982,945,1043,961,935,962,973,524,490,945,512,479,956,481,529,971,491,432,1031,971,982,940,1008,966,991,942,972,975,1012,943,1043,886,1033,940,1004,954,976,975,1009,942,974,486,545,924,516,427,1030,463,520,945,545,405,1000,522,527,909,970,974,1000,955,518,423,1069,924,971,486,516,951};
    private static final String DATA2 = "11111111111111111111001001001001001001111100111001110011100100111111111001001111111001111100111001001111111111111111111001001111100100111111111110010010010010011100111111111110011100111";

    @Test
    public void testSignalListener() throws Exception {
        final OregonV2Message[] messages = new OregonV2Message[1];
        MessageListener<OregonV2Message> messageListener = new MessageListener<OregonV2Message>() {
            @Override
            public void onMessage(OregonV2Message message) {
                messages[0] = (OregonV2Message) message.clone();
            }
        };
        OregonV2SignalListenerFactory factory = new OregonV2SignalListenerFactory();
        OregonV2SignalListener signalLengthListener = factory.createListener(messageListener);

         // custom changes for my THN132N
        signalLengthListener.getProperties().minPreambuleSize = 20;
        signalLengthListener.getProperties().packetSize = 68;

        for (long l : DATA)
        {
            signalLengthListener.onSignal(false, l);
        }

        OregonV2Message message = messages[0];
        assertEquals("ea4c", message.getDeviceType());
        assertEquals(28.3d, message.getTemperature(), 0d);
    }

    @Test
    public void testSignalListener2() throws Exception {
        final OregonV2Message[] messages = new OregonV2Message[1];
        MessageListener<OregonV2Message> messageListener = new MessageListener<OregonV2Message>() {
            @Override
            public void onMessage(OregonV2Message message) {
                messages[0] = (OregonV2Message) message.clone();
            }
        };
        OregonV2SignalListenerFactory factory = new OregonV2SignalListenerFactory();
        OregonV2SignalListener signalLengthListener = factory.createListener(messageListener);

         // custom changes for my THN132N
        signalLengthListener.getProperties().minPreambuleSize = 20;
        signalLengthListener.getProperties().packetSize = 68;

        for (byte b : DATA2.getBytes())
        {
            signalLengthListener.onSignal(false, b == '1' ? 1000 : 500);
        }

        OregonV2Message message = messages[0];
        assertEquals("ea4c", message.getDeviceType());
        assertEquals(22.0d, message.getTemperature(), 0d);
    }
}
