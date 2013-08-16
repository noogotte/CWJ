package fr.aumgn.cwj.plugin;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.aumgn.cwj.CWJ;

public class PluginManager {

    private final Path               folder;
    private final List<PluginLoader> pluginLoaders;
    private List<Plugin>             plugins;

    public PluginManager(Path serverFolder) {
        this.folder = serverFolder.resolve("plugins");

        ServiceLoader<PluginLoader> pluginLoadersLoader = ServiceLoader.load(PluginLoader.class);
        pluginLoaders = Lists.newArrayList(pluginLoadersLoader);
    }

    public void load() {
        CWJ.getLogger().info("Loading plugins using loaders :");
        for (PluginLoader pluginLoader : pluginLoaders) {
            CWJ.getLogger().info(" - " + pluginLoader.getClass().getName());
        }

        if (!folder.toFile().exists()) {
            CWJ.getLogger().log(Level.INFO, "Unable to access `plugins` directory, creating the directory");
            folder.toFile().mkdir();
        }

        DirectoryStream<Path> pluginPaths;
        try {
            pluginPaths = Files.newDirectoryStream(folder);
        }
        catch (IOException exc) {
            CWJ.getLogger().log(Level.SEVERE, "Unable to access `plugins` directory", exc);
            return;
        }

        ImmutableList.Builder<Plugin> pluginsBuilder = ImmutableList.builder();
        for (Path path : pluginPaths) {
            if (path.getFileName().toString().startsWith(".")) {
                continue;
            }

            try {
                Plugin plugin = loadPlugin(path);
                if (plugin == null) {
                    CWJ.getLogger().warning("Unable to find suitable plugin loader for file " + path.getFileName());
                }
                else {
                    pluginsBuilder.add(plugin);
                    CWJ.getLogger().info("Plugin " + pluginName(plugin) + " loaded.");
                }
            }
            catch (PluginLoaderException exc) {
                CWJ.getLogger().log(Level.SEVERE, "Error while loading for file " + path.getFileName(), exc);
            }
        }

        this.plugins = pluginsBuilder.build();
        CWJ.getLogger().info(plugins.size() + " plugins loaded");
    }

    private Plugin loadPlugin(Path path) {
        for (PluginLoader pluginLoader : pluginLoaders) {
            Plugin plugin = pluginLoader.load(path);
            if (plugin != null) {
                return plugin;
            }
        }

        return null;
    }

    private String pluginName(Plugin plugin) {
        PluginDescriptor descriptor = plugin.getDescriptor();
        return descriptor.getName() + " v" + descriptor.getVersion();
    }

    public void enableAll() {
        for (Plugin plugin : plugins) {
            plugin.enable();
        }

    }

    public void disableAll() {
        for (Plugin plugin : plugins) {
            plugin.disable();
        }
    }
}
