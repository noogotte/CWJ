package fr.aumgn.cwj.i18n;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.aumgn.cwj.CWJ;

/**
 * Class which handles loading of Localization resources for the given target
 * and locale.
 */
public class I18n {

    private static final Locale    UNIVERSAL_LOCALE = Locale.US;
    private static final Charset   CHARSET          = Charsets.UTF_8;

    private final List<I18nLoader> loaders;
    private final I18nTarget       target;
    private final Locale[]         locales;

    public static I18n create(I18nTarget target, Locale... locales) {
        return new I18n(target, locales);
    }

    private I18n(I18nTarget target, Locale... locales) {
        this.loaders = Lists.newArrayList(ServiceLoader.load(I18nLoader.class));
        this.target = checkNotNull(target);
        this.locales = localesLookupFor(locales);
    }

    /**
     * Loads the resources with the given name.
     */
    public Localization load(String name) {
        return new Localization(loadForEachLocale(name));
    }

    private ImmutableMap.Builder<String, MessageFormat> loadForEachLocale(String name) {
        ImmutableMap.Builder<String, MessageFormat> map = ImmutableMap.<String, MessageFormat> builder();
        for (Locale locale : locales) {
            loadForEachLoader(map, locale, name + "_" + locale.toString());
        }
        return map;
    }

    private void loadForEachLoader(ImmutableMap.Builder<String, MessageFormat> map, Locale locale, String baseName) {
        for (I18nLoader loader : loaders) {
            loadForEachExtension(map, locale, baseName, loader);
        }
    }

    private void loadForEachExtension(ImmutableMap.Builder<String, MessageFormat> map, Locale locale, String baseName,
            I18nLoader loader) {
        for (String extension : loader.getExtensions()) {
            String name = baseName + "." + extension;
            loadForEachLocation(map, locale, loader, name);
        }
    }

    private void loadForEachLocation(ImmutableMap.Builder<String, MessageFormat> map, Locale locale, I18nLoader loader,
            String name) {
        for (URL location : target.getI18nLocations(name)) {
            actuallyLoad(map, loader, locale, location);
        }
    }

    private void actuallyLoad(ImmutableMap.Builder<String, MessageFormat> map, I18nLoader loader, Locale locale,
            URL location) {
        try (Reader reader = reader(location)) {
            putAll(map, loader.load(reader), locale);
        }
        catch (IOException exc) {
            CWJ.getLogger().log(Level.SEVERE, "Unable to read localization data for location : " + location, exc);
        }
    }

    private Reader reader(URL location) throws IOException {
        URLConnection connection = location.openConnection();
        connection.setUseCaches(false);
        InputStream inputStream = connection.getInputStream();
        return new InputStreamReader(inputStream, CHARSET);
    }

    private void putAll(ImmutableMap.Builder<String, MessageFormat> map, Map<?, ?> loaded, Locale locale) {
        for (Entry<?, ?> entry : loaded.entrySet()) {
            String key = entry.getKey().toString();
            String message = entry.getValue().toString();
            map.put(key, parse(locale, message));
        }
    }

    private MessageFormat parse(Locale locale, String rawMessage) {
        String message = rawMessage;
        message = message.replaceAll("'", "''");
        message = message.replaceAll("\\\\", "'");
        return new MessageFormat(message, locale);
    }

    private static Locale[] localesLookupFor(Locale... locales) {
        Set<Locale> localesSet = Sets.newLinkedHashSet();
        for (Locale locale : locales) {
            checkNotNull(locale);
            localesSet.add(locale);
            localesSet.add(new Locale(locale.getLanguage()));
        }
        localesSet.add(UNIVERSAL_LOCALE);
        localesSet.add(new Locale(UNIVERSAL_LOCALE.getLanguage()));

        Locale[] resolved = new Locale[localesSet.size()];
        int i = resolved.length - 1;
        for (Locale locale : localesSet) {
            resolved[i] = locale;
            i--;
        }
        return resolved;
    }
}
