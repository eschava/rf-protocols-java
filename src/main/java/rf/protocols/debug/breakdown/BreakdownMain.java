package rf.protocols.debug.breakdown;

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
public class BreakdownMain {

    public static void main(String[] args) throws InterruptedException {
        final BreakdownSignalListenerProperties properties = new BreakdownSignalListenerProperties();

        // load listener properties from -Dlistener.PROP parameters
        Properties props = System.getProperties();
        for (Object keyObj : props.keySet()) {
            String key = keyObj.toString();
            if (key.startsWith("listener.")) {
                String prop = key.substring("listener.".length());
                properties.setProperty(prop, props.getProperty(key));
            }
        }

        BreakdownSignalListener debugGroupsSignalListener = new BreakdownSignalListener(new PacketListener<BreakdownPacket>() {
            @Override
            public void onPacket(BreakdownPacket packet) {
                printPacket(properties, packet);
            }
        });
        debugGroupsSignalListener.setProperties(properties);

        SignalLevelListener signalListener = new SignalLengthAdapterLevelListener(debugGroupsSignalListener);

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        CubieboardPin pin = new CubieboardPin("PI14", 68, "I", 14, "68_pi14", true);
        pin.addFeature(new DigitalIOFeature(pin, new CubieboardDigitalInput(pin), new CubieboardDigitalOutput(pin)));
        board.getPins().add(pin);

        printHeader(debugGroupsSignalListener.getProperties());

        DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        debugGroupsSignalListener.start();
        input.enableInterrupts();
        input.addInterruptListener(new BulldogInterruptListener(signalListener));

        Thread.sleep(1000 * 1000 * 1000);
    }

    private static void printHeader(BreakdownSignalListenerProperties properties) {
        int length = properties.minLength;
        int diff = (properties.maxLength - properties.minLength) / properties.groupCount;

        if (length > 0) {
            System.out.print("<" + length);
            System.out.print("\t");
        }

        for (int i = 0; i < properties.groupCount; i++) {
            length += diff;
            System.out.print("<" + length);
            System.out.print("\t");
        }

        System.out.print(">=" + length);
        System.out.println();
    }

    private static void printPacket(BreakdownSignalListenerProperties properties, BreakdownPacket packet) {
        if (properties.minLength > 0) {
            System.out.print(packet.getLessThanGrouppedCount());
            System.out.print("\t");
        }

        for (int i = 0; i < properties.groupCount; i++) {
            System.out.print(packet.getGroupCount(i));
            System.out.print("\t");
        }

        System.out.print(packet.getMoreThanGrouppedCount());
        System.out.println();
    }
}
