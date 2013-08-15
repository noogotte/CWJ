package fr.aumgn.cwj.i18n;

import java.io.Reader;
import java.util.Map;

/**
 * Interfaces for classes which load messages for a specific format.
 */
public abstract class I18nLoader {

    /**
     * File extensions (without the dot) accepted by this loader.
     */
    public abstract String[] getExtensions();

    /**
     * Parse the reader as a Map of &lt;key, message&gt;.
     */
    public abstract Map<?, ?> load(Reader reader);
}
