package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Matcher getPatternMatch(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(value);
    }
}
