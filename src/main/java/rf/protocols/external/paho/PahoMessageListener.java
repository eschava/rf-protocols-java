package rf.protocols.external.paho;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.core.MessageMetaData;
import rf.protocols.external.ognl.MessageFormatter;
import rf.protocols.external.ognl.MissingObjectPropertyAccessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMessageListener implements MessageListener<Message> {
    static {
        MissingObjectPropertyAccessor.install();
    }

    private final MqttClient client;
    private final String topicTemplate;

    public PahoMessageListener(MqttClient client, String topicTemplate) {
        this.client = client;
        this.topicTemplate = topicTemplate;
    }

    @Override
    public void onMessage(Message message) {
        MessageMetaData<Message> metaData = (MessageMetaData<Message>) message.getMetaData();
        Collection<String> fieldNames = metaData.getFieldNames();

        try {
            for (String fieldName : fieldNames) {
                String value = metaData.isStringField(fieldName)
                        ? metaData.getStringField(message, fieldName)
                        : String.valueOf(metaData.getNumericField(message, fieldName));

                String topic = getTopic(message, fieldName);
                byte[] payload = value.getBytes();
                client.publish(topic, payload, 1, false);
            }
        } catch (MqttException e) {
            client.log.severe(getClass().getName(), "onMessage", "MQTT exception", null, e);
        }
    }

    private synchronized String getTopic(Message message, String fieldName) {
        Map<String, Object> root = new HashMap<String, Object>(2);
        root.put("message", message);
        root.put("field", fieldName);
        return MessageFormatter.format(topicTemplate, root);
    }
}
