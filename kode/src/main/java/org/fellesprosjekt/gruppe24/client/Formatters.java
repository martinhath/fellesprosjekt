package org.fellesprosjekt.gruppe24.client;

import java.time.format.DateTimeFormatter;

public class Formatters {

    public static DateTimeFormatter dayformat = DateTimeFormatter.ofPattern("EEEE d.");

    public static DateTimeFormatter weekformat = DateTimeFormatter.ofPattern("w");

    public static DateTimeFormatter monthformat = DateTimeFormatter.ofPattern("MMMM");

    public static DateTimeFormatter hhmmformat = DateTimeFormatter.ofPattern("HH:mm");

}
