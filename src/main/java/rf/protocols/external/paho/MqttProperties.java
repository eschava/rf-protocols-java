package rf.protocols.external.paho;

import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.SignalListenerRegistry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class MqttProperties extends AbstractProperties {
    public String mqttUrl = "tcp://localhost";
    public String mqttClientId = "rf-protocols";
    public String mqttUser = null;
    public String mqttPassword = null;
    public String sendTopicTemplate = "rf/<message.protocol>/<field>";
    public String receiveTopicTemplate = "rf/send/+";
    public String protocolNames = "all";
    public String adapter = null;
    public String inputPin;
    public String outputPin;

    @Override
    public void setProperty(String name, String value) {
        if (!name.contains(".")) {
            super.setProperty(name, value);
            return;
        }

        String[] parts = name.split("\\.", 2);
        String protocol = parts[0];
        name = parts[1];

        if (protocol.equals("adapter")) {
            Adapter adptr = AdapterRegistry.getInstance().getAdapter(adapter);
            adptr.setProperty(name, value);
        } else {
            SignalListenerRegistry.getInstance().setProtocolProperty(protocol, name, value);
        }
    }
}
