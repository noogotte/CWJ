package fr.aumgn.cwj.plugin;

import java.nio.file.Path;

public interface PluginLoader {

    Plugin load(Path path);
}
