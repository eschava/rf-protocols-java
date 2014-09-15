package rf.protocols.analysis.intervals;

import rf.protocols.core.PacketListener;
import rf.protocols.external.ognl.PropertiesConfigurer;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class IntervalsMain {

    public static void main(String[] args) throws InterruptedException, IOException {
        final IntervalsSignalListenerProperties properties = new IntervalsSignalListenerProperties();
        PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);
        final ExecutorService printService = Executors.newSingleThreadExecutor();

        // load properties
        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null)
            propertiesConfigurer.loadFromFile(propertiesFile);

        IntervalsSignalListener intervalSignalListener = new IntervalsSignalListener(new PacketListener<IntervalsPacket>() {
            @Override
            public void onPacket(final IntervalsPacket packet) {
                final IntervalsPacket clone = packet.clone();

                printService.execute(new Runnable() {
                    @Override
                    public void run() {
                        printPacket(properties, clone);
                    }
                });
            }
        });
        intervalSignalListener.setProperties(properties);
        intervalSignalListener.start();

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        adapter.addListener(properties.pin, intervalSignalListener);

        Thread.sleep(Long.MAX_VALUE);
    }

    private static void printPacket(IntervalsSignalListenerProperties properties, IntervalsPacket packet) {
        System.out.print("[");
        System.out.print(packet.getBeforePacketLength());
        System.out.print("]");

        boolean isFirst = true;
        boolean level = packet.getFirstSignalLevel();
        for (long length : packet.getLengths()) {
            // separator
            if (properties.namesSeparator != null) {
                if (!isFirst)
                    System.out.print(properties.namesSeparator);
                else
                    isFirst = false;
            }

            System.out.print(properties.getIntervalName(length, level));
            level = !level;
        }

        System.out.print("[");
        System.out.print(packet.getAfterPacketLength());
        System.out.print("]");

        System.out.print(" (");
        System.out.print(packet.getLengths().size());
        System.out.print(")");

        System.out.println();
    }
}
