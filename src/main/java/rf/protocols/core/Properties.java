package rf.protocols.core;

/**
 * Configurable properties of any component
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface Properties {
    void setProperty(String name, String value);
    Properties clone();
}
