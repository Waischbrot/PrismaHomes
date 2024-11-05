package net.prismaforge.libraries.reflection;



import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings({"unchecked", "unused"})
public final class ReflectionUtil {

    @Nullable
    public static <T> T getField(Object object,
                                 String fieldName) {
        return getField(object, object.getClass(), fieldName);
    }

    @Nullable
    public static <T> T getField(Object object,
                                 Class<?> clazz,
                                 String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            field.setAccessible(false);
            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void setField(Class<?> clazz,
                                    String fieldName,
                                    T value) {
        try {

            Field field = clazz.getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();

            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void setField(Object object,
                                    String fieldName,
                                    T value) {
        try {

            Field field = object.getClass().getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();

            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static <T> T invoke(Object object,
                               String methodName,
                               Object... params) {
        return invoke(object.getClass(), object, methodName, params);
    }

    @Nullable
    public static <T> T invoke(Class<?> clazz,
                               Object object,
                               String methodName,
                               Object... params) {

        Class<?>[] paramTypes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++)
            paramTypes[i] = params[i].getClass();
        return invoke(clazz, object, methodName, paramTypes, params);
    }

    @Nullable
    public static <T> T invoke(Class<?> clazz,
                               Object object,
                               String methodName,
                               Class<?>[] paramTypes,
                               Object... params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            boolean accessible = method.isAccessible();

            method.setAccessible(true);
            Object value = method.invoke(object, params);
            method.setAccessible(accessible);

            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T invoke(Object object,
                               Method method,
                               Object... params) {
        try {
            boolean accessible = method.isAccessible();

            method.setAccessible(true);
            Object value = method.invoke(object, params);
            method.setAccessible(accessible);

            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
