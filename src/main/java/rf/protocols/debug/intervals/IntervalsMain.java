package rf.protocols.debug.intervals;

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
import rf.protocols.core.impl.SignalLengthAdapterLevelListener;

import java.util.Properties;

/**
 * @author Eugeny.Schava
 */
public class IntervalsMain {

    public static void main(String[] args) throws InterruptedException {
        final IntervalsSignalListenerProperties properties = new IntervalsSignalListenerProperties();

        // load listener properties from -Dlistener.PROP parameters
        Properties props = System.getProperties();
        for (Object keyObj : props.keySet()) {
            String key = keyObj.toString();
            if (key.startsWith("listener.")) {
                String prop = key.substring("listener.".length());
                properties.setProperty(prop, props.getProperty(key));
            }
        }

        IntervalsSignalListener intervalSignalListener = new IntervalsSignalListener(new PacketListener<IntervalsPacket>() {
            @Override
            public void onPacket(IntervalsPacket packet) {
                printPacket(properties, packet);
            }
        });
        intervalSignalListener.setProperties(properties);

        SignalLevelListener signalListener = new SignalLengthAdapterLevelListener(intervalSignalListener);

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        CubieboardPin pin = new CubieboardPin("PI14", 68, "I", 14, "68_pi14", true);
        pin.addFeature(new DigitalIOFeature(pin, new CubieboardDigitalInput(pin), new CubieboardDigitalOutput(pin)));
        board.getPins().add(pin);

        DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        intervalSignalListener.start();
        input.enableInterrupts();
        input.addInterruptListener(new BulldogInterruptListener(signalListener));

        Thread.sleep(1000 * 1000 * 1000);
    }

    private static void printPacket(IntervalsSignalListenerProperties properties, IntervalsPacket packet) {
        System.out.print(packet.getBeforePacketLength());
        System.out.print("-");

        boolean isFirst = true;
        for (String intervalName : packet.getIntervals()) {
            // separator
            if (!isFirst) {
                if (properties.namesSeparator != null)
                    System.out.print(properties.namesSeparator);
            } else {
                isFirst = false;
            }

            System.out.print(intervalName);
        }

        System.out.print("-");
        System.out.print(packet.getAfterPacketLength());

        System.out.print(" (");
        System.out.print(packet.getIntervals().size());
        System.out.print(")");

        System.out.println();
    }
}
