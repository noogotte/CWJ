package fr.aumgn.cwj.i18n;

import static com.google.common.base.Preconditions.checkNotNull;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * A set of messages loaded with {@link I18n} for a given {@link Locale}.
 */
public class Localization {

    private final Map<String, MessageFormat> map;

    public Localization(Map<String, MessageFormat> map) {
        this.map = checkNotNull(map);
    }

    public boolean has(String key) {
        return map.containsKey(checkNotNull(key));
    }

    public String get(String key) {
        return get(key, new Object[0]);
    }

    public String get(String key, Object... arguments) {
        String message = rawGet(key, arguments);
        if (message == null) {
            return keyNotFound(key);
        }

        return message;
    }

    public String get(String[] keys) {
        return get(keys, new Object[0]);
    }

    public String get(String[] keys, Object... arguments) {
        Preconditions.checkArgument(keys.length > 0, "Keys can't be empty");
        for (String key : keys) {
            String message = rawGet(key, arguments);
            if (message != null) {
                return message;
            }
        }

        return keyNotFound(keys[keys.length - 1]);
    }

    private String keyNotFound(String key) {
        return "## Missing message for key \"" + key + "\" ##";
    }

    public String rawGet(String key) {
        return rawGet(key, new Object[0]);
    }

    public String rawGet(String key, Object... arguments) {
        if (!map.containsKey(key)) {
            return null;
        }

        MessageFormat message = map.get(key);
        return message.format(arguments);
    }

    public Set<String> keys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public Set<Entry<String, MessageFormat>> entries() {
        return Collections.unmodifiableSet(map.entrySet());
    }
}
