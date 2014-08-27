package rf.protocols.pt2262;

import org.bulldog.core.gpio.DigitalInput;
import org.bulldog.core.gpio.base.DigitalIOFeature;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.cubieboard.CubieboardPin;
import org.bulldog.cubieboard.gpio.CubieboardDigitalInput;
import org.bulldog.cubieboard.gpio.CubieboardDigitalOutput;
import rf.protocols.bulldog.BulldogInterruptListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.core.impl.BitPacket;
import rf.protocols.core.impl.SignalLengthAdapterLevelListener;

import java.util.Properties;

/**
 * @author Eugeny.Schava
 */
public class PT2262Main {

    public static void main(String[] args) throws InterruptedException {
        final PT2262SignalListenerProperties properties = new PT2262SignalListenerProperties();

        // load listener properties from -Dlistener.PROP parameters
        Properties props = System.getProperties();
        for (Object keyObj : props.keySet()) {
            String key = keyObj.toString();
            if (key.startsWith("listener.")) {
                String prop = key.substring("listener.".length());
                properties.setProperty(prop, props.getProperty(key));
            }
        }

        PT2262SignalListener pt2262SignalListener = new PT2262SignalListener(new PacketListener<BitPacket>() {
            @Override
            public void onPacket(BitPacket packet) {
                System.out.println(packet.toString());
            }
        });
        pt2262SignalListener.setProperties(properties);

        SignalLevelListener signalListener = new SignalLengthAdapterLevelListener(pt2262SignalListener);

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        CubieboardPin pin = new CubieboardPin("PI14", 68, "I", 14, "68_pi14", true);
        pin.addFeature(new DigitalIOFeature(pin, new CubieboardDigitalInput(pin), new CubieboardDigitalOutput(pin)));
        board.getPins().add(pin);

        DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        input.enableInterrupts();
        input.addInterruptListener(new BulldogInterruptListener(signalListener));

        Thread.sleep(1000 * 1000 * 1000);
    }

}
