package rf.protocols.analysis;

import rf.protocols.core.Message;
import rf.protocols.core.MessageListener;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.external.Adapter;
import rf.protocols.external.ognl.PropertiesConfigurer;
import rf.protocols.external.ognl.PropertiesWithAdapterConfigurer;
import rf.protocols.registry.AdapterRegistry;
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
        PropertiesConfigurer propertiesConfigurer = new PropertiesWithAdapterConfigurer(properties);

        // load properties
        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null)
            propertiesConfigurer.loadFromFile(propertiesFile);
        // load listener properties from -Dlistener.PROP parameters
        propertiesConfigurer.loadFromSystemProperties("listener.");

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
        SignalLengthListener signalLengthListener = registry.createListener(messageListener, protocolNames);

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        adapter.addListener(signalLengthListener);

        Thread.sleep(Long.MAX_VALUE);
    }

    public static class Properties extends AbstractProperties {
        public String adapter;
    }
}
