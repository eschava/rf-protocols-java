package rf.protocols.analysis;

import org.bulldog.core.gpio.DigitalInput;
import org.bulldog.core.gpio.Pin;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.cubieboard.Cubieboard;
import rf.protocols.external.bulldog.BulldogInterruptListener;
import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.registry.SignalListenerRegistry;
import rf.protocols.core.SignalLevelListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PrintAllMessages {
    public static void main(String[] args) throws InterruptedException, IOException {
        SignalListenerRegistry registry = SignalListenerRegistry.getInstance();
        final ExecutorService printService = Executors.newSingleThreadExecutor();

        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null) {
            Properties props = new Properties();
            props.load(new FileInputStream(propertiesFile));

            for (String key : props.stringPropertyNames())
            {
                String[] parts = key.split(".", 2);
                String protocol = parts[0];
                String name = parts[1];
                String value = props.getProperty(key);

                registry.setProtocolProperty(protocol, name, value);
            }
        }

        Collection<String> protocolNames = registry.getProtocolNames();
        MessageListener<? extends Message> messageListener = new MessageListener<Message>() {
            @Override
            public void onMessage(Message message) {
                final Message clone = message.clone();
                printService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(clone);
                    }
                });
            }
        };
        SignalLevelListener signalLevelListener = registry.createListener(messageListener, protocolNames);

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        Pin pin = ((Cubieboard) board).createDigitalIOPin("PI14", 68, "I", 14, "68_pi14", true);
        board.getPins().add(pin);

        DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        input.setInterruptDebounceMs(-1);
        input.enableInterrupts();
        input.addInterruptListener(new BulldogInterruptListener(signalLevelListener));

        Thread.sleep(1000 * 1000 * 1000l);
    }
}
