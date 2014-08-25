package rf.protocols.core.impl;

import rf.protocols.core.SignalListenerProperties;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;

/**
 * Helper implementation of {@link rf.protocols.core.SignalListenerProperties}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AbstractSignalListenerProperties implements SignalListenerProperties, Cloneable {

    @Override
    public void setProperty(String name, String value) {
        try {
            Field field = getClass().getField(name);
            PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());
            editor.setAsText(value);
            Object objValue = editor.getValue();
            field.set(this, objValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignalListenerProperties clone() {
        try {
            return (SignalListenerProperties) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
