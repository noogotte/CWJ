package fr.aumgn.cwj.i18n;

import java.net.URL;

public interface I18nTarget {

    Iterable<URL> getI18nLocations(String filename);
}
