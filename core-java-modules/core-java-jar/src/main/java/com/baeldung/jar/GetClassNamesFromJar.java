package com.baeldung.jar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GetClassNamesFromJar {

    public static Set<String> getClasseNamesFromJarFile(File givenFile) throws IOException {
        Set<String> classeNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                                               .replace("/", ".")
                                               .replace(".class", "");
                    classeNames.add(className);
                }
            }
            return classeNames;
        }
    }

    public static Set<Class> getClassesFromJarFile(File jarFile) throws IOException, ClassNotFoundException {
        Set<String> classeNames = getClasseNamesFromJarFile(jarFile);
        Set<Class> classes = new HashSet<>(classeNames.size());
        try (URLClassLoader cl = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + jarFile + "!/") })) {
            for (String name : classeNames) {
                Class clazz = cl.loadClass(name); // Loading the class by its name
                classes.add(clazz);
            }
        }
        return classes;
    }
}
