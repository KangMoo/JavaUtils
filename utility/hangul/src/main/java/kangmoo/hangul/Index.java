package kangmoo.hangul;

public class Index {
    static Pair<String, String> excludeLastElement(String array) {
        if (array.isEmpty()) {
            return new Pair<>("", "");
        }
        if (array.length() == 1) {
            return new Pair<>("", array);
        }
        return new Pair<>(array.substring(0, array.length() - 1), array.substring(array.length() - 1));
    }

    public static String joinString(String... args) {
        return String.join("", args);
    }

    public static boolean isBlank(String character) {
        return character.isBlank();
    }

    public static void assertCondition(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage != null ? errorMessage : "Invalid condition");
        }
    }
}
