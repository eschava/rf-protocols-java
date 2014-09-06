package rf.protocols.external.paho;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.SignalListenerRegistry;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
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
        if (propertiesFile != null)
            properties.loadFromFile(propertiesFile);

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

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);

        Collection<String> protocolNames = properties.protocolNames.equals("all")
                ? listenerRegistry.getProtocolNames()
                : Arrays.asList(properties.protocolNames.split(","));
        SignalLevelListener signalListener = listenerRegistry.createListener(messageListener, protocolNames);
        adapter.addListener(properties.inputPin, signalListener);

        SignalLengthSender signalSender = adapter.getSignalSender(properties.outputPin);
        mqttClient.setCallback(new PahoMessageCallback(mqttClient.log, signalSender, properties.receiveTopicTemplate));
        mqttClient.subscribe(properties.receiveTopicTemplate);

        Thread.sleep(Long.MAX_VALUE);
    }
}
