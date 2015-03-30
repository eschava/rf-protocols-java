package rf.protocols.registry;

import rf.protocols.core.*;
import rf.protocols.core.impl.SignalLengthAdapterLevelListener;
import rf.protocols.core.impl.SignalLengthListenerGroup;
import rf.protocols.core.impl.SignalLevelListenerGroup;
import rf.protocols.device.generic.intervals.IntervalsSignalListenerFactory;
import rf.protocols.device.generic.intervalsequence.IntervalSequenceSignalListenerFactory;
import rf.protocols.device.lacrosse.LacrosseSignalListenerFactory;
import rf.protocols.device.oregon.sl109.OregonSL109SignalListenerFactory;
import rf.protocols.device.oregon.v2.OregonV2SignalListenerFactory;
import rf.protocols.device.owl.OwlSignalListenerFactory;
import rf.protocols.device.remoteswitch.RemoteSwitchSignalListenerFactory;
import rf.protocols.registry.interfaces.ConceptSignalListenerFactory;
import rf.protocols.registry.interfaces.SignalLengthListenerFactory;
import rf.protocols.registry.interfaces.SignalLevelListenerFactory;
import rf.protocols.registry.interfaces.SignalListenerFactory;

import java.util.*;

/**
 * Registry of listeners for all user accessible protocols
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SignalListenerRegistry {
    private static final SignalListenerRegistry INSTANCE = new SignalListenerRegistry();

    private final Set<String> factoryNames = new HashSet<String>();
    private final Map<String, SignalLevelListenerFactory> signalListenerFactoryMap = new HashMap<String, SignalLevelListenerFactory>();
    private final Map<String, SignalLengthListenerFactory> signalLengthListenerFactoryMap = new HashMap<String, SignalLengthListenerFactory>();

    public static SignalListenerRegistry getInstance() {
        return INSTANCE;
    }

    private SignalListenerRegistry() {
        // register all known factories
        registerFactory(new OregonSL109SignalListenerFactory());
        registerFactory(new OregonV2SignalListenerFactory());
//        registerFactory(new OregonV3SignalListenerFactory());
        registerFactory(new OwlSignalListenerFactory());
//        registerFactory(new PT2262SignalListenerFactory()); // deprecated, use RemoteSwitch
        registerFactory(new LacrosseSignalListenerFactory());
        registerFactory(new RemoteSwitchSignalListenerFactory());
        registerFactory(new IntervalsSignalListenerFactory());
        registerFactory(new IntervalSequenceSignalListenerFactory());
    }

    public void registerFactory(SignalLevelListenerFactory signalLevelListenerFactory) {
        registerFactory(signalLevelListenerFactory, signalListenerFactoryMap);
    }

    public void registerFactory(SignalLengthListenerFactory signalLengthListenerFactory) {
        registerFactory(signalLengthListenerFactory, signalLengthListenerFactoryMap);
    }

    private <F extends SignalListenerFactory> void registerFactory(F listenerFactory, Map<String, F> map) {
        if (!(listenerFactory instanceof ConceptSignalListenerFactory)) // skip concept factories
            factoryNames.add(listenerFactory.getProtocol());
        map.put(listenerFactory.getProtocol(), listenerFactory);
    }

    public boolean cloneProtocol(String oldName, String newName) {
        if (signalListenerFactoryMap.containsKey(oldName)) {
            SignalLevelListenerFactory listenerFactory = signalListenerFactoryMap.get(oldName);
            listenerFactory = listenerFactory.clone(newName);
            factoryNames.add(newName);
            signalListenerFactoryMap.put(newName, listenerFactory);
            return true;
        }

        if (signalLengthListenerFactoryMap.containsKey(oldName)) {
            SignalLengthListenerFactory listenerFactory = signalLengthListenerFactoryMap.get(oldName);
            listenerFactory = listenerFactory.clone(newName);
            factoryNames.add(newName);
            signalLengthListenerFactoryMap.put(newName, listenerFactory);
            return true;
        }

        return false;
    }

    public boolean setProtocolProperty(String protocol, String property, String value) {
        if (signalListenerFactoryMap.containsKey(protocol)) {
            SignalLevelListenerFactory listenerFactory = signalListenerFactoryMap.get(protocol);
            listenerFactory.setProperty(property, value);
            return true;
        }

        if (signalLengthListenerFactoryMap.containsKey(protocol)) {
            SignalLengthListenerFactory listenerFactory = signalLengthListenerFactoryMap.get(protocol);
            listenerFactory.setProperty(property, value);
            return true;
        }

        return false;
    }

    public Collection<String> getProtocolNames() {
        return Collections.unmodifiableSet(factoryNames);
    }

    public boolean isProtocolRegistered(String name) {
        return factoryNames.contains(name);
    }

    @SuppressWarnings("unchecked")
    public <M extends Message> SignalLevelListener createListener(MessageListener<M> messageListener, String protocol) {
        if (signalListenerFactoryMap.containsKey(protocol)) {
            SignalLevelListenerFactory listenerFactory = signalListenerFactoryMap.get(protocol);
            return listenerFactory.createListener(messageListener);
        }

        if (signalLengthListenerFactoryMap.containsKey(protocol)) {
            SignalLengthListenerFactory listenerFactory = signalLengthListenerFactoryMap.get(protocol);
            SignalLengthListener signalLengthListener = listenerFactory.createListener(messageListener);
            return new SignalLengthAdapterLevelListener(signalLengthListener);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet> SignalLevelListener createListener(PacketListener<P> packetListener, String protocol) {
        if (signalListenerFactoryMap.containsKey(protocol)) {
            SignalLevelListenerFactory listenerFactory = signalListenerFactoryMap.get(protocol);
            return listenerFactory.createListener(packetListener);
        }

        if (signalLengthListenerFactoryMap.containsKey(protocol)) {
            SignalLengthListenerFactory listenerFactory = signalLengthListenerFactoryMap.get(protocol);
            SignalLengthListener signalLengthListener = listenerFactory.createListener(packetListener);
            return new SignalLengthAdapterLevelListener(signalLengthListener);
        }

        return null;
    }

    public <M extends Message> SignalLevelListener createListener(MessageListener<M> messageListener, Collection<String> protocols) {
        if (protocols.size() == 1)
            return createListener(messageListener, protocols.iterator().next());

        List<SignalLevelListener> signalLevelListeners = new ArrayList<SignalLevelListener>();
        Set<String> listenerNamesLeft = new HashSet<String>(protocols);

        // signal length listeners
        List<SignalLengthListener> signalLengthListeners = new ArrayList<SignalLengthListener>();
        for (Iterator<String> iterator = listenerNamesLeft.iterator(); iterator.hasNext(); ) {
            String listenerName = iterator.next();
            if (signalLengthListenerFactoryMap.containsKey(listenerName)) {
                SignalLengthListenerFactory listenerFactory = signalLengthListenerFactoryMap.get(listenerName);
                SignalLengthListener signalLengthListener = listenerFactory.createListener(messageListener);
                signalLengthListeners.add(signalLengthListener);
                iterator.remove();
            }
        }

        if (signalLengthListeners.size() > 0) {
            SignalLengthListener lengthListener = signalLengthListeners.size() == 1
                    ? signalLengthListeners.get(0)
                    : new SignalLengthListenerGroup(signalLengthListeners);
            signalLevelListeners.add(new SignalLengthAdapterLevelListener(lengthListener));
        }

        // signal listeners
        for (Iterator<String> iterator = listenerNamesLeft.iterator(); iterator.hasNext(); ) {
            String listenerName = iterator.next();
            if (signalListenerFactoryMap.containsKey(listenerName)) {
                SignalLevelListenerFactory listenerFactory = signalListenerFactoryMap.get(listenerName);
                signalLevelListeners.add(listenerFactory.createListener(messageListener));
                iterator.remove();
            }
        }

        // TODO: check if listenerNamesLeft is empty

        return signalLevelListeners.size() == 1
                ? signalLevelListeners.get(0)
                : new SignalLevelListenerGroup(signalLevelListeners);
    }
}
