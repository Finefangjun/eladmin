package stm.bot.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringUtilsTest {

    @Test
    public void testToCamelCase() {
        assertNull(StringUtils.toCamelCase(null));
    }

    @Test
    public void testToCapitalizeCamelCase() {
        assertNull(StringUtils.toCapitalizeCamelCase(null));
        Assertions.assertEquals("HelloWorld", StringUtils.toCapitalizeCamelCase("hello_world"));
    }

    @Test
    public void testToUnderScoreCase() {
        assertNull(StringUtils.toUnderScoreCase(null));
        Assertions.assertEquals("hello_world", StringUtils.toUnderScoreCase("helloWorld"));
        Assertions.assertEquals("\u0000\u0000", StringUtils.toUnderScoreCase("\u0000\u0000"));
        Assertions.assertEquals("\u0000_a", StringUtils.toUnderScoreCase("\u0000A"));
    }

    @Test
    public void testGetWeekDay() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        Assertions.assertEquals(simpleDateformat.format(new Date()), StringUtils.getWeekDay());
    }

    @Test
    public void testGetIP() {
        Assertions.assertEquals("127.0.0.1", StringUtils.getIp(new MockHttpServletRequest()));
    }
}
