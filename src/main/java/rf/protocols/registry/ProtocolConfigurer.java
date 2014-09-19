package rf.protocols.registry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class ProtocolConfigurer {
    private static final ProtocolConfigurer INSTANCE = new ProtocolConfigurer();

    public static ProtocolConfigurer getInstance() {
        return INSTANCE;
    }

    private ProtocolConfigurer() {
    }

    public void setProtocolProperty(String protocol, String property, String value) {
//        // for using artificial property like CurrentName.clone=NewName
//        if (property.equals("clone")) {
//            cloneProtocol(protocol, value);
//            return;
//        }

        SignalListenerRegistry.getInstance().setProtocolProperty(protocol, property, value);
        StringMessageSenderRegistry.getInstance().setProtocolProperty(protocol, property, value);
    }

    public void cloneProtocol(String oldName, String newName) {
        if (!SignalListenerRegistry.getInstance().cloneProtocol(oldName, newName))
            throw new RuntimeException("Cannot clone listener protocol " + oldName + " because it isn't registered");
        if (!StringMessageSenderRegistry.getInstance().cloneProtocol(oldName, newName))
            throw new RuntimeException("Cannot clone sender protocol " + oldName + " because it isn't registered");
    }
}
