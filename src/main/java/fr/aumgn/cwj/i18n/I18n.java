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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import fr.aumgn.cwj.CWJ;

/**
 * Class which handles loading of Localization resources for the given target
 * and locale.
 */
public class I18n {

    private static final Locale  DEFAULT_LOCALE = Locale.US;
    private static final Charset CHARSET        = Charsets.UTF_8;

    public static Locale[] localesLookupFor(Locale targetLocale, Locale fallbackLocale) {
        Set<Locale> localesSet = new LinkedHashSet<Locale>(4);
        localesSet.add(targetLocale);
        localesSet.add(new Locale(targetLocale.getLanguage()));
        localesSet.add(fallbackLocale);
        localesSet.add(new Locale(fallbackLocale.getLanguage()));
        Locale[] locales = new Locale[localesSet.size()];
        int i = locales.length - 1;
        for (Locale locale : localesSet) {
            locales[i] = locale;
            i--;
        }
        return locales;
    }

    private final List<I18nLoader> loaders;
    private final I18nTarget       target;
    private final Locale[]         locales;

    public static I18n create(I18nTarget target) {
        return create(target, DEFAULT_LOCALE);
    }

    public static I18n create(I18nTarget target, Locale targetLocale) {
        return create(target, targetLocale, DEFAULT_LOCALE);
    }

    public static I18n create(I18nTarget target, Locale targetLocale, Locale fallbackLocale) {
        return new I18n(target, targetLocale, fallbackLocale);
    }

    private I18n(I18nTarget target, Locale targetLocale, Locale fallbackLocale) {
        this.loaders = Lists.newArrayList(ServiceLoader.load(I18nLoader.class));
        this.target = checkNotNull(target);
        this.locales = localesLookupFor(checkNotNull(targetLocale), checkNotNull(fallbackLocale));
    }

    /**
     * Loads the resources with the given name.
     */
    public Localization get(String name) {
        return new Localization(load(name));
    }

    private Map<String, MessageFormat> load(String name) {
        Map<String, MessageFormat> map = new HashMap<String, MessageFormat>();
        for (Locale locale : locales) {
            load(map, locale, name + "_" + locale.toString());
        }
        return map;
    }

    private void load(Map<String, MessageFormat> map, Locale locale, String baseName) {
        for (I18nLoader loader : loaders) {
            for (String extension : loader.getExtensions()) {
                String name = baseName + "." + extension;
                for (URL location : target.getI18nLocations(name)) {
                    tryLoad(map, loader, locale, location);
                }
            }
        }
    }

    private void tryLoad(Map<String, MessageFormat> map, I18nLoader loader, Locale locale, URL location) {
        try (Reader reader = reader(location)) {
            map.putAll(loader.load(locale, reader));
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
}
