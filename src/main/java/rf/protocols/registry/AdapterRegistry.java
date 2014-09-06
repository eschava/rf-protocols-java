package rf.protocols.registry;

import rf.protocols.external.Adapter;
import rf.protocols.external.bulldog.BulldogAdapter;
import rf.protocols.external.pi4j.PI4JAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AdapterRegistry {
    private static final AdapterRegistry INSTANCE = new AdapterRegistry();

    private final Map<String, Adapter> adapterMap = new HashMap<String, Adapter>();

    public static AdapterRegistry getInstance() {
        return INSTANCE;
    }

    private AdapterRegistry() {
        registerAdapter(new BulldogAdapter());
        registerAdapter(new PI4JAdapter());
    }

    private void registerAdapter(Adapter adapter) {
        adapterMap.put(adapter.getName(), adapter);
    }

    public Adapter getAdapter(String name) {
        return adapterMap.get(name);
    }
}
