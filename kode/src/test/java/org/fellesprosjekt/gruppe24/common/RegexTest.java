package org.fellesprosjekt.gruppe24.common;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;

public class RegexTest {

    @Before
    public void setup() {

    }

    @Test
    public void testTimeRegex() {
        Matcher m;
        String s;

        s = " 13:52   ";
        m = Regexes.Time.matcher(s);
        assertEquals(m.matches(), true);
        assertEquals(m.group(2), "13");
        assertEquals(m.group(3), "52");

        s = "a 15:23 huehue";
        m = Regexes.Time.matcher(s);
        assertEquals(m.matches(), false);
    }
}
