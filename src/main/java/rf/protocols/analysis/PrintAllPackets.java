package rf.protocols.analysis;

import rf.protocols.core.Packet;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.core.impl.SignalLengthListenerGroup;
import rf.protocols.external.Adapter;
import rf.protocols.external.ognl.PropertiesConfigurer;
import rf.protocols.external.ognl.PropertiesWithAdapterConfigurer;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.SignalListenerRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PrintAllPackets {
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

        Collection<SignalLengthListener> listeners = new ArrayList<SignalLengthListener>();
        SignalLengthListenerGroup listenerGroup = new SignalLengthListenerGroup(listeners);
        Collection<String> protocolNames = registry.getProtocolNames();

        for (final String protocolName : protocolNames)
        {
            PacketListener<?> packetListener = new PacketListener<Packet>() {
                @Override
                public void onPacket(final Packet packet) {
                    final Packet clone = packet.clone();

                    printService.execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(new Date() + " " + protocolName + ": " + clone);
                        }
                    });
                }
            };

            SignalLengthListener signalLevelListener = registry.createListener(packetListener, protocolName);
            listeners.add(signalLevelListener);
        }

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        adapter.addListener(listenerGroup);

        Thread.sleep(Long.MAX_VALUE);
    }

    public static class Properties extends AbstractProperties {
        public String adapter;
    }
}
