package rf.protocols.registry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PropertyConfigurer {
    public static void setProtocolProperty(String protocol, String property, String value) {
        // for using artificial property like CurrentName.clone=NewName
        if (property.equals("clone")) {
            cloneProtocol(property, value);
            return;
        }

        SignalListenerRegistry.getInstance().setProtocolProperty(protocol, property, value);
        StringMessageSenderRegistry.getInstance().setProtocolProperty(protocol, property, value);
    }

    private static void cloneProtocol(String oldName, String newName) {
        SignalListenerRegistry.getInstance().cloneProtocol(oldName, newName);
        StringMessageSenderRegistry.getInstance().cloneProtocol(oldName, newName);
    }
}
