package rf.protocols.device.oregon;

import org.junit.Test;
import rf.protocols.core.MessageListener;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.device.oregon.v2.OregonV2MessageFactory;
import rf.protocols.device.oregon.v2.OregonV2SignalListener;
import rf.protocols.device.oregon.v2.OregonV2SignalListenerFactory;
import rf.protocols.device.oregon.v2.message.OregonV2AbstractMessage;
import rf.protocols.device.oregon.v2.message.OregonV2TemperatureHumidityMessage;
import rf.protocols.device.oregon.v2.message.OregonV2TemperatureMessage;

import static org.junit.Assert.assertEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OregonV2Test {
    private static final long[] DATA = {949,1020,929,969,973,1008,952,971,972,1042,913,1006,939,1036,920,974,973,1002,954,990,972,1035,923,998,942,1009,946,975,973,1008,952,510,459,1031,467,455,1001,495,456,972,523,458,1005,496,466,967,479,515,973,982,943,1006,971,494,462,969,1060,907,481,513,978,948,972,518,487,951,973,1003,515,436,979,516,451,1015,940,1004,986,971,910,1034,919,1039,484,486,968,502,455,1039,916,975,973,1032,925,978,483,547,922,970,977,1000,957,516,456,998,957,1009,478,484,955,514,425,1064,923,1005,490,510,921,517,463,988,940,999,541,436,970,975,1001,496,455,975,1010,982,945,1043,961,935,962,973,524,490,945,512,479,956,481,529,971,491,432,1031,971,982,940,1008,966,991,942,972,975,1012,943,1043,886,1033,940,1004,954,976,975,1009,942,974,486,545,924,516,427,1030,463,520,945,545,405,1000,522,527,909,970,974,1000,955,518,423,1069,924,971,486,516,951};
    private static final String DATA2 = "11111111111111111111001001001001001001111100111001110011100100111111111001001111111001111100111001001111111111111111111001001111100100111111111110010010010010011100111111111110011100111";

    @Test
    public void testSignalListener() throws Exception {
        final OregonV2TemperatureMessage[] messages = new OregonV2TemperatureMessage[1];
        MessageListener<OregonV2TemperatureMessage> messageListener = new MessageListener<OregonV2TemperatureMessage>() {
            @Override
            public void onMessage(OregonV2TemperatureMessage message) {
                messages[0] = (OregonV2TemperatureMessage) message.clone();
            }
        };
        OregonV2SignalListenerFactory factory = new OregonV2SignalListenerFactory();
        OregonV2SignalListener signalLengthListener = factory.createListener(messageListener);

         // custom changes for my THN132N
        signalLengthListener.getProperties().minPreambuleSize = 20;

        for (long l : DATA)
            signalLengthListener.onSignal(false, l);
        signalLengthListener.onSignal(false, -1);

        OregonV2TemperatureMessage message = messages[0];
        assertEquals("ec40", message.getDeviceType());
        assertEquals(1, message.getChannelId());
        assertEquals(78, message.getRollingId());
        assertEquals(false, message.isBatteryLow());
        assertEquals(28.3d, message.getTemperature(), 0d);
    }

    @Test
    public void testSignalListener2() throws Exception {
        final OregonV2TemperatureMessage[] messages = new OregonV2TemperatureMessage[1];
        MessageListener<OregonV2TemperatureMessage> messageListener = new MessageListener<OregonV2TemperatureMessage>() {
            @Override
            public void onMessage(OregonV2TemperatureMessage message) {
                messages[0] = (OregonV2TemperatureMessage) message.clone();
            }
        };
        OregonV2SignalListenerFactory factory = new OregonV2SignalListenerFactory();
        OregonV2SignalListener signalLengthListener = factory.createListener(messageListener);

         // custom changes for my THN132N
        signalLengthListener.getProperties().minPreambuleSize = 20;

        for (byte b : DATA2.getBytes())
            signalLengthListener.onSignal(false, b == '1' ? 1000 : 500);
        signalLengthListener.onSignal(false, -1);

        OregonV2TemperatureMessage message = messages[0];
        assertEquals("ec40", message.getDeviceType());
        assertEquals(1, message.getChannelId());
        assertEquals(78, message.getRollingId());
        assertEquals(false, message.isBatteryLow());
        assertEquals(22.0d, message.getTemperature(), 0d);
        assertEquals(22.0d, message.getMetaData().getNumericField(message, "Temperature"), 0d);
    }

    @Test
    public void testTemperatureHumidityMessage() throws Exception {
        String data = "1A2D10B6051970A345";
        OregonV2TemperatureHumidityMessage message = (OregonV2TemperatureHumidityMessage) getMessage(data);
        assertEquals("1d20", message.getDeviceType());
        assertEquals(1, message.getChannelId());
        assertEquals(182, message.getRollingId());
        assertEquals(true, message.isBatteryLow());
        assertEquals(19.0d, message.getTemperature(), 0d);
        assertEquals(19.0d, message.getMetaData().getNumericField(message, "Temperature"), 0d);
        assertEquals(37.0d, message.getHumidity(), 0d);
        assertEquals(37.0d, message.getMetaData().getNumericField(message, "Humidity"), 0d);
    }

    protected OregonV2AbstractMessage getMessage(String data) {
        BitPacket packet = new BitPacket(100);

        for (int i = 0; i < data.length(); i += 2) {
            String bs = data.substring(i, i + 2);
            int b = Integer.parseInt(bs, 16);
            for (int j = 0; j < 8; j++) {
                packet.addBit((b & 1) == 1);
                b >>= 1;
            }
        }

        OregonV2AbstractMessage message = new OregonV2MessageFactory("test").createMessage(packet);
        return message.isValid() ? message : null;
    }
}
