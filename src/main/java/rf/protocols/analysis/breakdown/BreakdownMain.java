package rf.protocols.analysis.breakdown;

import rf.protocols.core.PacketListener;
import rf.protocols.external.Adapter;
import rf.protocols.registry.AdapterRegistry;

import java.io.IOException;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BreakdownMain {

    public static void main(String[] args) throws InterruptedException, IOException {
        final BreakdownSignalListenerProperties properties = new BreakdownSignalListenerProperties();

        // load properties
        String propertiesFile = System.getProperty("propertiesFile");
        if (propertiesFile != null)
            properties.loadFromFile(propertiesFile);

        BreakdownSignalListener debugGroupsSignalListener = new BreakdownSignalListener(new PacketListener<BreakdownPacket>() {
            @Override
            public void onPacket(BreakdownPacket packet) {
                printPacket(properties, packet);
            }
        });

        debugGroupsSignalListener.setProperties(properties);
        printHeader(properties);
        debugGroupsSignalListener.start();

        Adapter adapter = AdapterRegistry.getInstance().getAdapter(properties.adapter);
        adapter.addListener(properties.pin, debugGroupsSignalListener);

        Thread.sleep(1000 * 1000 * 1000l);
    }

    private static void printHeader(BreakdownSignalListenerProperties properties) {
        int length = properties.minLength;
        int diff = (properties.maxLength - properties.minLength) / properties.groupCount;

        if (length > 0) {
            System.out.print("<" + length);
            System.out.print("\t");
        }

        for (int i = 0; i < properties.groupCount; i++) {
            length += diff;
            System.out.print("<" + length);
            System.out.print("\t");
        }

        System.out.print(">=" + length);
        System.out.println();
    }

    private static void printPacket(BreakdownSignalListenerProperties properties, BreakdownPacket packet) {
        if (properties.minLength > 0) {
            System.out.print(packet.getLessThanGrouppedCount());
            System.out.print("\t");
        }

        for (int i = 0; i < properties.groupCount; i++) {
            System.out.print(packet.getGroupCount(i));
            System.out.print("\t");
        }

        System.out.print(packet.getMoreThanGrouppedCount());
        System.out.println();
    }
}
