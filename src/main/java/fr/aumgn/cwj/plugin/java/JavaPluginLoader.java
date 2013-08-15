package fr.aumgn.cwj.plugin.java;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import fr.aumgn.cwj.plugin.Plugin;
import fr.aumgn.cwj.plugin.PluginDescriptor;
import fr.aumgn.cwj.plugin.PluginLoader;
import fr.aumgn.cwj.plugin.PluginLoaderException;

public class JavaPluginLoader implements PluginLoader {

    private static final String DESCRIPTOR_FILENAME = "plugin.properties";

    public JavaPluginLoader() {
    }

    private String pluginName(PluginDescriptor descriptor) {
        return descriptor.getName() + " v" + descriptor.getVersion();
    }

    @Override
    public JavaPlugin load(Path path) {
        if (Files.isDirectory(path)) {
            return loadDirectory(path);
        }
        else if (path.getFileName().toString().endsWith(".jar")) {
            return loadJar(path);
        }

        return null;
    }

    private JavaPlugin loadDirectory(Path path) {
        Path propertiesPath = path.resolve(DESCRIPTOR_FILENAME);
        if (!Files.exists(propertiesPath)) {
            return null;
        }

        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(propertiesPath));
        }
        catch (IOException exc) {
            throw new PluginLoaderException("Error while reading properties file", exc);
        }

        return doLoad(path.toUri(), properties);
    }

    private JavaPlugin loadJar(Path path) {
        Properties properties = new Properties();

        try (JarFile jarFile = new JarFile(path.toFile())) {
            ZipEntry descriptorEntry = jarFile.getEntry(DESCRIPTOR_FILENAME);
            properties.load(jarFile.getInputStream(descriptorEntry));
        }
        catch (IOException exc) {
            throw new PluginLoaderException("Unable to read descriptor file in jar " + path.getFileName(), exc);
        }

        return doLoad(path.toUri(), properties);
    }

    private JavaPlugin doLoad(URI uri, Properties properties) {
        JavaPluginDescriptor descriptor = new JavaPluginDescriptor(properties);
        JavaPluginClassLoader classLoader = createClassLoader(descriptor, uri);
        Class<?> mainClass = loadMainClass(classLoader, descriptor);
        return instantiatePlugin(mainClass, classLoader, descriptor);
    }

    private JavaPluginClassLoader createClassLoader(JavaPluginDescriptor descriptor, URI uri) {
        try {
            return new JavaPluginClassLoader(Plugin.class.getClassLoader(), uri.toURL());
        }
        catch (MalformedURLException exc) {
            throw new PluginLoaderException("Error while creating class loader for plugin " + pluginName(descriptor),
                    exc);
        }
    }

    private Class<?> loadMainClass(ClassLoader loader, JavaPluginDescriptor descriptor) {
        try {
            return loader.loadClass(descriptor.getMainClass());
        }
        catch (ClassNotFoundException exc) {
            throw new PluginLoaderException("Unable to load main class " + descriptor.getMainClass(), exc);
        }
    }

    private JavaPlugin instantiatePlugin(Class<?> rawClass, JavaPluginClassLoader classLoader,
            JavaPluginDescriptor descriptor) {
        if (!JavaPlugin.class.isAssignableFrom(rawClass)) {
            throw new PluginLoaderException("Main class is not subclass of JavaPlugin");
        }
        Class<? extends JavaPlugin> pluginClass = rawClass.asSubclass(JavaPlugin.class);

        JavaPlugin instance;
        try {
            instance = pluginClass.newInstance();
        }
        catch (SecurityException | InstantiationException | IllegalAccessException exc) {
            throw new PluginLoaderException("Cannot find suitable contructor for class" + pluginClass.getName()
                    + " in plugin " + pluginName(descriptor), exc);
        }

        try {
            Method method = JavaPlugin.class.getDeclaredMethod("initialize", JavaPluginClassLoader.class,
                    JavaPluginDescriptor.class);
            method.setAccessible(true);
            method.invoke(instance, classLoader, descriptor);
            return instance;
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException exc) {
            throw new PluginLoaderException("Cannot instantiate " + pluginClass.getName() + " in plugin "
                    + pluginName(descriptor), exc);
        }
    }
}
