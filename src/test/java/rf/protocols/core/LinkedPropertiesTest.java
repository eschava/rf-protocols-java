package rf.protocols.core;

import org.junit.Test;
import rf.protocols.core.impl.LinkedProperties;

import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LinkedPropertiesTest {
    @Test
    public void testStringPropertyNames() throws Exception {
        LinkedProperties properties = new LinkedProperties();
        properties.load(new StringReader(
                "ten=a\ngreen=b\nbootles=c\nhanging=d\non=e\nthe=f\nwall=g\n"));

        String[] values = new String[properties.size()];
        int i = 0;
        for(String name : properties.stringPropertyNames()) {
            String value = properties.getProperty(name);
            values[i++] = value;
        }

        String[] sortedValues = values.clone();
        Arrays.sort(sortedValues);

        assertArrayEquals(values, sortedValues);
    }
}
