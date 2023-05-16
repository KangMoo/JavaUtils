package com.github.kangmoo.utils.common;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author kangmoo Heo
 */
public class PackageClassReaderUtil {
    private PackageClassReaderUtil() {
    }

    public static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<Class<?>> classes = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equalsIgnoreCase("file")) {
                classes.addAll(findClasses(new File(resource.getFile()), packageName));
            } else if (resource.getProtocol().equalsIgnoreCase("jar")) {
                classes.addAll(findClasses(resource, packageName));
            }
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> findClasses(URL resource, String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        JarFile jarFile = conn.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        String path = packageName.replace('.', '/');
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(path) && name.endsWith(".class")) {
                String className = name.replace('/', '.').substring(0, name.length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
}
