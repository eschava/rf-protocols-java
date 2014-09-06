package rf.protocols.external.paho;

import rf.protocols.core.impl.AbstractProperties;

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
}
