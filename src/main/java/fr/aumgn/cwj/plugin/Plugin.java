package fr.aumgn.cwj.plugin;

public interface Plugin {

    ClassLoader getClassLoader();

    PluginDescriptor getDescriptor();

    void enable();

    void disable();
}
