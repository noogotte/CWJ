package fr.aumgn.cwj.plugin;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;

import com.google.common.collect.Lists;

import fr.aumgn.cwj.CWJ;

public class PluginManager {

    private final Path               folder;
    private final List<PluginLoader> pluginLoaders;

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

        DirectoryStream<Path> pluginPaths;
        try {
            pluginPaths = Files.newDirectoryStream(folder);
        }
        catch (IOException exc) {
            CWJ.getLogger().log(Level.SEVERE, "Unable to acces `plugins` directory", exc);
            return;
        }

        List<Plugin> plugins = Lists.newArrayList();
        for (Path path : pluginPaths) {
            try {
                Plugin plugin = loadPlugin(path);
                if (plugin == null) {
                    CWJ.getLogger().warning("Unable to find suitable plugin loader for file " + path.getFileName());
                }
                else {
                    plugins.add(plugin);
                    CWJ.getLogger().info("Plugin " + pluginName(plugin) + " loaded.");
                }
            }
            catch (PluginLoaderException exc) {
                CWJ.getLogger().log(Level.SEVERE, "Error while loading for file " + path.getFileName(), exc);
            }
        }

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
}
