package rf.protocols.debug.intervals;

import org.bulldog.core.Signal;
import org.bulldog.core.gpio.DigitalInput;
import org.bulldog.core.gpio.Pin;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.cubieboard.Cubieboard;
import rf.protocols.bulldog.BulldogInterruptListener;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.core.impl.SignalLengthAdapterLevelListener;

import java.util.Properties;

/**
 * @author Eugene Schava <eschava@gmail.com>
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

        final SignalLevelListener signalListener = new SignalLengthAdapterLevelListener(intervalSignalListener);

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        final Pin pin = ((Cubieboard) board).createDigitalIOPin("PI14", 68, "I", 14, "68_pi14", true);
        board.getPins().add(pin);

        final DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        input.setInterruptDebounceMs(-1);
        intervalSignalListener.start();

        if (properties.useInterrupts) {
            input.enableInterrupts();
            input.addInterruptListener(new BulldogInterruptListener(signalListener));
        } else {
            Thread readingThread = new Thread() {
                @Override
                public void run() {
                    Signal oldValue = null;
                    while (true) {
                        Signal value = input.read();
                        if (value != oldValue) {
                            signalListener.onSignal(value.getBooleanValue());
                            oldValue = value;
                        }
                    }
                }
            };
            readingThread.setDaemon(true);
            readingThread.start();
        }

        Thread.sleep(1000 * 1000 * 1000);
    }

    private static void printPacket(IntervalsSignalListenerProperties properties, IntervalsPacket packet) {
        System.out.print("[");
        System.out.print(packet.getBeforePacketLength());
        System.out.print("]");

        boolean isFirst = true;
        for (String intervalName : packet.getIntervals()) {
            // separator
            if (properties.namesSeparator != null) {
                if (!isFirst)
                    System.out.print(properties.namesSeparator);
                else
                    isFirst = false;
            }

            System.out.print(intervalName);
        }

        System.out.print("[");
        System.out.print(packet.getAfterPacketLength());
        System.out.print("]");

        System.out.print(" (");
        System.out.print(packet.getIntervals().size());
        System.out.print(")");

        System.out.println();
    }
}
