package rf.protocols.core.impl;

import rf.protocols.core.Properties;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helper implementation of {@link rf.protocols.core.Properties}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AbstractProperties implements Properties, Cloneable {

    @Override
    public void loadFromFile(String fileName) throws IOException {
        java.util.Properties props = new java.util.Properties();
        props.load(new FileInputStream(fileName));

        loadFromProperties(props);
    }

    protected void loadFromProperties(java.util.Properties props) {
        for (String key : props.stringPropertyNames())
        {
            String value = props.getProperty(key);
            setProperty(key, value);
        }
    }

    @Override
    public void setProperty(String name, String value) {
        try {
            if (name.contains(".")) {
                String[] parts = name.split("\\.", 2);
                Object target = getClass().getField(parts[0]).get(this);

                try {
                    setVariable(target, parts[1], value);
                } catch (NoSuchFieldException e) {
                    setMethod(target, parts[1], value);
                }
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

        boolean methodFound = false;
        for (Method method : target.getClass().getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
                PropertyEditor editor = PropertyEditorManager.findEditor(method.getParameterTypes()[0]);
                editor.setAsText(value);
                Object arg = editor.getValue();

                method.invoke(target, arg);
                methodFound = true;
                break;
            }
        }

        if (!methodFound)
            throw new RuntimeException("Method " + methodName + " is not found");
    }

    @Override
    public Properties clone() {
        try {
            return (Properties) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
