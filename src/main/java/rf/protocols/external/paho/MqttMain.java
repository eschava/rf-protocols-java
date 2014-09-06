package rf.protocols.external.paho;

import org.bulldog.core.gpio.DigitalInput;
import org.bulldog.core.gpio.DigitalOutput;
import org.bulldog.core.gpio.Pin;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.cubieboard.Cubieboard;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.external.bulldog.BulldogInterruptListener;
import rf.protocols.external.bulldog.BulldogSignalSender;
import rf.protocols.registry.SignalListenerRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class MqttMain {
    public static void main(String[] args) throws InterruptedException, MqttException, IOException {
        SignalListenerRegistry listenerRegistry = SignalListenerRegistry.getInstance();
        MqttProperties properties = new MqttProperties();

        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null) {
            Properties props = new Properties();
            props.load(new FileInputStream(propertiesFile));

            for (String key : props.stringPropertyNames())
            {
                String value = props.getProperty(key);
                String[] parts = key.split("\\.", 2);

                if (parts.length == 1) {
                    properties.setProperty(key, value);
                } else {
                    String protocol = parts[0];
                    String name = parts[1];

                    listenerRegistry.setProtocolProperty(protocol, name, value);
                }
            }
        }

        MqttClient mqttClient = new MqttClient(properties.mqttUrl, properties.mqttClientId);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(properties.mqttUser);
        if (properties.mqttPassword != null)
            mqttConnectOptions.setPassword(properties.mqttPassword.toCharArray());
        mqttClient.connect();

        final PahoMessageListener pahoMessageListener = new PahoMessageListener(mqttClient, properties.sendTopicTemplate);
        final ExecutorService sendService = Executors.newSingleThreadExecutor();

        MessageListener<? extends Message> messageListener = new MessageListener<Message>() {
            @Override
            public void onMessage(Message message) {
                final Message clone = message.clone();
                sendService.execute(new Runnable() {
                    @Override
                    public void run() {
                        pahoMessageListener.onMessage(clone);
                    }
                });
            }
        };

        Collection<String> protocolNames = properties.protocolNames.equals("all")
                ? listenerRegistry.getProtocolNames()
                : Arrays.asList(properties.protocolNames.split(","));
        SignalLevelListener signalListener = listenerRegistry.createListener(messageListener, protocolNames);

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        Pin pin = ((Cubieboard) board).createDigitalIOPin("PI14", 68, "I", 14, "68_pi14", true);
        board.getPins().add(pin);

        DigitalInput input = board.getPin("PI14").as(DigitalInput.class);
        input.setInterruptDebounceMs(-1); // TODO: remove with new version of libbuldog
        input.enableInterrupts();
        input.addInterruptListener(new BulldogInterruptListener(signalListener));

        // TODO: pins should be moved to configuration
        DigitalOutput output = board.getPin("PG2").as(DigitalOutput.class);
        SignalLengthSender signalSender = new BulldogSignalSender(output);

        mqttClient.setCallback(new PahoMessageCallback(mqttClient.log, signalSender, properties.receiveTopicTemplate));
        mqttClient.subscribe(properties.receiveTopicTemplate);

        Thread.sleep(Long.MAX_VALUE);
    }
}
