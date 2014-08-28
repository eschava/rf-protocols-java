package rf.protocols.core.impl;

import rf.protocols.core.SignalListenerProperties;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helper implementation of {@link rf.protocols.core.SignalListenerProperties}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AbstractSignalListenerProperties implements SignalListenerProperties, Cloneable {

    @Override
    public void setProperty(String name, String value) {
        try {
            if (name.contains(".")) {
                String[] parts = name.split("\\.", 2);
                Object target = getClass().getField(parts[0]).get(this);
                setMethod(target, parts[1], value);
            } else {
                setVariable(this, name, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setVariable(Object target, String name, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getField(name);
        PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());
        editor.setAsText(value);
        Object objValue = editor.getValue();
        field.set(target, objValue);
    }

    private void setMethod(Object target, String name, String value) throws Exception {
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
                PropertyEditor editor = PropertyEditorManager.findEditor(method.getParameterTypes()[0]);
                editor.setAsText(value);
                Object arg = editor.getValue();

                method.invoke(target, arg);
                break;
            }
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
