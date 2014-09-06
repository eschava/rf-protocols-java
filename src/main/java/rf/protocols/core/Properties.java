package rf.protocols.core;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Configurable properties of any component
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface Properties {
    void setProperty(String name, String value);
    void loadFromFile(String fileName) throws IOException;
    Properties clone();
}
