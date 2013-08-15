package fr.aumgn.cwj.i18n;

import static com.google.common.base.Preconditions.checkNotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * A set of messages loaded with {@link I18n} for a given {@link Locale}.
 */
public class Localization {

    private final Map<String, MessageFormat> map;

    public Localization(ImmutableMap.Builder<String, MessageFormat> mapBuilder) {
        this.map = checkNotNull(mapBuilder).build();
    }

    public boolean has(String key) {
        return map.containsKey(checkNotNull(key));
    }

    public String get(String key, Object... arguments) {
        String message = rawGet(key, arguments);
        if (message == null) {
            return keyNotFound(key);
        }

        return message;
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

    private String rawGet(String key, Object... arguments) {
        checkNotNull(key);
        if (!map.containsKey(key)) {
            return null;
        }

        MessageFormat message = map.get(key);
        return message.format(arguments);
    }

    public Set<String> keys() {
        return map.keySet();
    }
}
