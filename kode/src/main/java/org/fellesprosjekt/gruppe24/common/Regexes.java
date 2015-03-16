package org.fellesprosjekt.gruppe24.common;

import java.util.regex.Pattern;

public class Regexes {
    /**
     * Klasse for å holde RegEx patterns, siden vi sikkert
     * kommer til å bruke de samme patternsene i flere kontrollere.
     */

    // matcher ' 13:52  '
    public static Pattern Time = Pattern.compile("\\s*((\\d\\d):(\\d\\d))\\s*");
}
