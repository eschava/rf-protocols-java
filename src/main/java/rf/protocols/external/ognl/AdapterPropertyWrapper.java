package rf.protocols.external.ognl;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AdapterPropertyWrapper {
    private String adapter;
    private StringBuilder name = new StringBuilder();

    public AdapterPropertyWrapper(String adapter) {
        this.adapter = adapter;
    }

    public String getAdapter() {
        return adapter;
    }

    public void addProperty(Object property) {
        if (property instanceof Integer) {
            name.append('[').append(property).append(']');
        } else {
            if (name.length() > 0)
                name.append('.');
            name.append(property);
        }
    }

    public String getProperty(Object property) {
        addProperty(property);
        return name.toString();
    }
}
