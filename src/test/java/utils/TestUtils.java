package utils;

/**
 * @author Aleksander
 */
public class TestUtils {

    public static String createStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append("a");
        }

        return sb.toString();
    }
}
