package rf.protocols.external.paho;

import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class MqttProperties extends AbstractProperties {
    public String mqttUrl = "tcp://localhost";
    public String mqttClientId = "rf-protocols";
    public String mqttUser = null;
    public String mqttPassword = null;
    public String sendTopicTemplate = "rf/${message.protocol}/${field}";
    public String receiveTopicTemplate = "rf/send/+";
    public String protocolNames = "all";
    public String adapter = null;
    public String sendAdapter = null;
    public String receiveAdapter = null;

    public Adapter getReceiveAdapter() {
        if (receiveAdapter != null)
            return AdapterRegistry.getInstance().getAdapter(receiveAdapter);
        return getAdapter();
    }

    public Adapter getSendAdapter() {
        if (sendAdapter != null)
            return AdapterRegistry.getInstance().getAdapter(sendAdapter);
        return getAdapter();
    }

    private Adapter getAdapter() {
        if (adapter != null)
            return AdapterRegistry.getInstance().getAdapter(adapter);
        return null;
    }
}
