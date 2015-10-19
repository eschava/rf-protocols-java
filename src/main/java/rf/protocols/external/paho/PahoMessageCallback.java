package rf.protocols.external.paho;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.registry.StringMessageSenderRegistry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMessageCallback implements MqttCallback {
    private final Logger log;
    private final SignalLengthSender signalSender;
    private String topicTemplate;

    public PahoMessageCallback(Logger log, SignalLengthSender signalSender, String topicTemplate) {
        this.log = log;
        this.signalSender = signalSender;
        this.topicTemplate = topicTemplate;
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        try {
            String sender = getProtocol(topic);
            String message = new String(mqttMessage.getPayload());

            StringMessageSenderRegistry.getInstance().sendMessage(sender, message, signalSender);
        } catch (Exception e) {
            log.severe(getClass().getName(), "messageArrived", e.getMessage(), null, e);
        }
    }

    String getProtocol(String topic) {
        int wildcardPos = topicTemplate.indexOf('+');
        return topic.substring(wildcardPos, topic.length() - topicTemplate.length() + wildcardPos + 1);
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.severe(getClass().getName(), "connectionLost", "Connection lost", null, cause);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
