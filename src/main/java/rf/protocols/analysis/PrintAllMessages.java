package rf.protocols.analysis;

import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.ProtocolConfigurer;
import rf.protocols.registry.SignalListenerRegistry;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PrintAllMessages {
    public static void main(String[] args) throws InterruptedException, IOException {
        SignalListenerRegistry registry = SignalListenerRegistry.getInstance();
        final ExecutorService printService = Executors.newSingleThreadExecutor();
        Properties properties = new Properties();

        // load properties
        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null)
            properties.loadFromFile(propertiesFile);

        Collection<String> protocolNames = registry.getProtocolNames();
        MessageListener<? extends Message> messageListener = new MessageListener<Message>() {
            @Override
            public void onMessage(Message message) {
                final Message clone = message.clone();
                printService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(new Date() + " " + clone);
                    }
                });
            }
        };
        SignalLevelListener signalLevelListener = registry.createListener(messageListener, protocolNames);

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        adapter.addListener(properties.pin, signalLevelListener);

        Thread.sleep(Long.MAX_VALUE);
    }

    public static class Properties extends AbstractProperties {
        public String adapter;
        public String pin;

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
                ProtocolConfigurer.setProtocolProperty(protocol, name, value);
            }
        }
    }
}
