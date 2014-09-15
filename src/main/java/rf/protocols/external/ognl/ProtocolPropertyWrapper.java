package rf.protocols.external.ognl;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class ProtocolPropertyWrapper {
    private String protocol;
    private StringBuilder name = new StringBuilder();

    public ProtocolPropertyWrapper(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void addProperty(Object property) {
        if (name.length() > 0)
            name.append('.');
        name.append(property);
    }

    public String getProperty(Object property) {
        addProperty(property);
        return name.toString();
    }
}
