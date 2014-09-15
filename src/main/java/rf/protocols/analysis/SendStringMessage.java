package rf.protocols.analysis;

import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.ognl.PropertiesConfigurer;
import rf.protocols.external.ognl.PropertiesWithAdapterConfigurer;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.StringMessageSenderRegistry;

import java.io.IOException;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SendStringMessage {

    public static void main(String[] args) throws IOException {
        String sender = System.getProperty("protocol");
        String message = System.getProperty("message");
        String propertiesFile = System.getProperty("propertiesFile");

        // load properties
        Properties properties = new Properties();
        PropertiesConfigurer propertiesConfigurer = new PropertiesWithAdapterConfigurer(properties);
        if (propertiesFile != null)
            propertiesConfigurer.loadFromFile(propertiesFile);

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        SignalLengthSender signalSender = adapter.getSignalSender(properties.pin);

        StringMessageSenderRegistry.getInstance().sendMessage(sender, message, signalSender);
    }

    public static class Properties extends AbstractProperties {
        public String adapter;
        public String pin;
    }
}