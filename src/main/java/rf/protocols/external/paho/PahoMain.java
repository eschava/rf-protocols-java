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

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMain {
    public static void main(String[] args) throws InterruptedException, MqttException {
        String mqttUrl = System.getProperty("mqttUrl");
        String mqttClientId = System.getProperty("mqttClientId");
        String mqttUser = System.getProperty("mqttUser");
        String mqttPassword = System.getProperty("mqttPassword");
        String sendTopicTemplate = System.getProperty("sendTopicTemplate");
        String receiveTopicTemplate = System.getProperty("receiveTopicTemplate");

        MqttClient mqttClient = new MqttClient(mqttUrl, mqttClientId);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(mqttUser);
        if (mqttPassword != null)
            mqttConnectOptions.setPassword(mqttPassword.toCharArray());
        mqttClient.connect();

        final PahoMessageListener pahoMessageListener = new PahoMessageListener(mqttClient, sendTopicTemplate);
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

        SignalListenerRegistry listenerRegistry = SignalListenerRegistry.getInstance();
        Collection<String> protocolNames = listenerRegistry.getProtocolNames();
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

        mqttClient.setCallback(new PahoMessageCallback(mqttClient.log, signalSender, receiveTopicTemplate));
        mqttClient.subscribe(receiveTopicTemplate);

        Thread.sleep(Long.MAX_VALUE);
    }
}
