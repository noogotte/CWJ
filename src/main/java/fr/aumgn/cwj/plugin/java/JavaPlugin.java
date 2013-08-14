package fr.aumgn.cwj.plugin.java;

import fr.aumgn.cwj.plugin.Plugin;

public class JavaPlugin implements Plugin {

    private final JavaPluginClassLoader classLoader;
    private final JavaPluginDescriptor  descriptor;

    public JavaPlugin(JavaPluginClassLoader classLoader, JavaPluginDescriptor descriptor) {
        this.classLoader = classLoader;
        this.descriptor = descriptor;
    }

    @Override
    public final JavaPluginClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public final JavaPluginDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }
}
