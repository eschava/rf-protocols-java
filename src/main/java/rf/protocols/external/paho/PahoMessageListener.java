package rf.protocols.external.paho;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.stringtemplate.v4.ST;
import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.core.MessageMetaData;

import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PahoMessageListener implements MessageListener<Message> {
    private final MqttClient client;
    private final ST topicTemplate;

    public PahoMessageListener(MqttClient client, String topicTemplate) {
        this.client = client;
        this.topicTemplate = new ST(topicTemplate);
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
        try {
            topicTemplate.add("message", message);
            topicTemplate.add("field", fieldName);
            return topicTemplate.render();
        } finally {
            topicTemplate.remove("message");
            topicTemplate.remove("field");
        }
    }
}
