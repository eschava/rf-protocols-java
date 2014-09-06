package rf.protocols.analysis;

import org.bulldog.core.pinfeatures.DigitalInput;
import org.bulldog.core.pinfeatures.Pin;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.cubieboard.Cubieboard;
import rf.protocols.core.Packet;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.core.impl.SignalLevelListenerGroup;
import rf.protocols.external.bulldog.BulldogInterruptListener;
import rf.protocols.registry.SignalListenerRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PrintAllPackets {
    public static void main(String[] args) throws InterruptedException, IOException {
        SignalListenerRegistry registry = SignalListenerRegistry.getInstance();
        final ExecutorService printService = Executors.newSingleThreadExecutor();

        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null) {
            Properties props = new Properties();
            props.load(new FileInputStream(propertiesFile));

            for (String key : props.stringPropertyNames())
            {
                String[] parts = key.split("\\.", 2);
                String protocol = parts[0];
                String name = parts[1];
                String value = props.getProperty(key);

                registry.setProtocolProperty(protocol, name, value);
            }
        }

        Collection<SignalLevelListener> listeners = new ArrayList<SignalLevelListener>();
        SignalLevelListenerGroup listenerGroup = new SignalLevelListenerGroup(listeners);
        Collection<String> protocolNames = registry.getProtocolNames();

        for (final String protocolName : protocolNames)
        {
            PacketListener<?> packetListener = new PacketListener<Packet>() {
                @Override
                public void onPacket(final Packet packet) {
                    final Packet clone = packet.clone();

                    printService.execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(protocolName + ": " + clone);
                        }
                    });
                }
            };

            SignalLevelListener signalLevelListener = registry.createListener(packetListener, protocolName);
            listeners.add(signalLevelListener);
        }

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        Pin pin = ((Cubieboard) board).createDigitalIOPin("PI14", 68, "I", 14, "68_pi14", true);
        board.getPins().add(pin);

        DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        input.setInterruptDebounceMs(-1);
        input.enableInterrupts();
        input.addInterruptListener(new BulldogInterruptListener(listenerGroup));

        Thread.sleep(Long.MAX_VALUE);
    }
}
