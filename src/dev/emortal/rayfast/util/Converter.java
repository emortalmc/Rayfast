package dev.emortal.rayfast.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Used to generate converters of specified types
 *
 * @param <C>
 */
public class Converter<C> {

    public Converter() {}

    private final Map<Class<?>, Function<?, C>> convertingFunctions = new ConcurrentHashMap<>();

    /**
     * Registers a conversion function for a specific class
     *
     * @param clazz the class of the converter function
     * @param converterFunction the converter function
     * @param <T> the class
     */
    public <T> void register(Class<T> clazz, Function<T, C> converterFunction) {
        convertingFunctions.put(clazz, converterFunction);
    }


    /**
     * Converts an object to a Area3d.
     * <br><br>
     * The object needs to have a registered converting function. These
     * functions can be registered using #register
     */
    @SuppressWarnings("unchecked")
    public <T> C from(T object) {

        final Class<?> originalClazz = object.getClass();
        Class<?> clazz = originalClazz;

        while (clazz != null) {

            Function<Object, C> convertingFunction = (Function<Object, C>) convertingFunctions.get(clazz);

            if (convertingFunction != null) {
                if (clazz != originalClazz) {
                    convertingFunctions.put(originalClazz, convertingFunction);
                }

                return convertingFunction.apply(object);
            }

            clazz = clazz.getSuperclass();
        }

        throw new IllegalArgumentException(originalClazz.getName() + "object provided to Converter#from did not have a registered converter");
    }
}
