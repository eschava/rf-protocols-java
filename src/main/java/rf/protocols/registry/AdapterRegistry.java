package rf.protocols.registry;

import rf.protocols.external.Adapter;
import rf.protocols.external.bulldog.BulldogAdapter;
import rf.protocols.external.pi4j.PI4JAdapter;
import rf.protocols.external.sdrtrunk.SDRTrunkAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AdapterRegistry {
    private static final AdapterRegistry INSTANCE = new AdapterRegistry();

    private final Map<String, Class<? extends Adapter>> adapterClassMap = new HashMap<String, Class<? extends Adapter>>();
    private final Map<String, Adapter> adapterMap = new HashMap<String, Adapter>();

    public static AdapterRegistry getInstance() {
        return INSTANCE;
    }

    private AdapterRegistry() {
        registerAdapter("bulldog", BulldogAdapter.class);
        registerAdapter("pi4j", PI4JAdapter.class);
        registerAdapter("sdrtrunk", SDRTrunkAdapter.class);
    }

    public void registerAdapter(String name, Class<? extends Adapter> adapterClass) {
        adapterClassMap.put(name, adapterClass);
    }

//    private void registerAdapter(Adapter adapter) {
//        adapterMap.put(adapter.getName(), adapter);
//    }

    public Adapter getAdapter(String name) {
        Adapter adapter = adapterMap.get(name);

        if (adapter == null) {
            try {
                Class<? extends Adapter> clazz = adapterClassMap.get(name);
                if (clazz == null)
                    throw new IllegalArgumentException("Adapter with name=" + name + " is not registered");
                adapter = clazz.newInstance();
                adapterMap.put(name, adapter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return adapter;
    }
}
