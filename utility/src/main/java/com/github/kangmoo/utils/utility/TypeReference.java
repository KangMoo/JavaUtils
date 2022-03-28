package com.github.kangmoo.utils.utility;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author kangmoo Heo
 */
public abstract class TypeReference {
    private final Type[] types;
    private volatile Constructor constructor;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new IllegalArgumentException("Class should be anonymous or extended from a generic class");
        }
        this.types = ((ParameterizedType) superclass).getActualTypeArguments();
    }

    /**
     * Gets the referenced type of the first generic parameter
     */
    public Type getGenericType() {
        return getGenericType(0);
    }

    /**
     * Gets the referenced type of the n-th generic parameter
     */
    public Type getGenericType(final int n) {
        return this.types[n];
    }

    /**
     * Gets the referenced Class of the first generic parameter
     */
    public Class getGenericParameterClass() {
        return getGenericParameterClass(0);
    }

    /**
     * Gets the referenced Class of the n-th generic parameter
     */
    public Class getGenericParameterClass(final int n) {
        if (n >= types.length) {
            throw new IllegalArgumentException("Missing parameter");
        }
        Type type = types[n];

        Class clazz = (type instanceof Class) ? (Class) type
                : (Class) ((ParameterizedType) type).getRawType();
        if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0)
            throw new IllegalArgumentException("generic parameter must be a concrete class");
        return clazz;
    }

    /**
     * Instantiates a new instance of {@code T} using the first generic parameter
     */
    @SuppressWarnings("unchecked")
    public Class newGenericInstance()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return newGenericInstance(0);
    }

    /**
     * Instantiates a new instance of {@code T} using the n-th generic parameter
     */
    @SuppressWarnings("unchecked")
    public Class newGenericInstance(final int n)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (constructor == null) {
            constructor = getGenericParameterClass(n).getConstructor();
        }
        return (Class) constructor.newInstance();
    }

    /**
     * Instantiates a new instance of {@code T} using the n-th generic parameter
     */
    @SuppressWarnings("unchecked")
    public Class newGenericInstance(final int n, final Object... objects)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Type type = types[n];
        Class rawType = type instanceof Class ? (Class) type
                : (Class) ((ParameterizedType) type).getRawType();
        Class[] types = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            types[i] = objects[i].getClass();
        }

        constructor = rawType.getConstructor(types);
        return (Class) constructor.newInstance(objects);

    }

    /**
     * function identifies type of the typeNum -th generics in the paramNum parameter
     * @param paramNum
     *            parameter number
     * @param typeNum
     *            generics type in the paramNum parameter
     * @return Type of the
     */
    public Type getActualTypeParameters(int paramNum, int typeNum) {
        return ((ParameterizedType) getGenericType(paramNum))
                .getActualTypeArguments()[typeNum];
    }

    public boolean equals(Object o) {
        if (o instanceof TypeReference) {
            int len = ((TypeReference) o).types.length;
            if (len != types.length)
                return false;
            for (int i = 0; i < types.length; i++) {
                if (!((TypeReference) o).types[i].equals(this.types[i]))
                    return false;
            }

            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = 0;

        for (int i = 0; i < types.length; i++) {

            hash = (hash << 1) + types[i].hashCode();

        }
        return hash;
    }
}
