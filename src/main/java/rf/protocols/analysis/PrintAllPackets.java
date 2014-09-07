package rf.protocols.analysis;

import rf.protocols.core.Packet;
import rf.protocols.core.PacketListener;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.core.impl.AbstractProperties;
import rf.protocols.core.impl.SignalLevelListenerGroup;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;
import rf.protocols.registry.SignalListenerRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

        // load properties
        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null)
            properties.loadFromFile(propertiesFile);

        Collection<SignalLevelListener> listeners = new ArrayList<SignalLevelListener>();
        SignalLevelListenerGroup listenerGroup = new SignalLevelListenerGroup(listeners);
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
                            System.out.println(protocolName + ": " + clone);
                        }
                    });
                }
            };

            SignalLevelListener signalLevelListener = registry.createListener(packetListener, protocolName);
            listeners.add(signalLevelListener);
        }

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        adapter.addListener(properties.pin, listenerGroup);

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
                SignalListenerRegistry.getInstance().setProtocolProperty(protocol, name, value);
            }
        }
    }
}
