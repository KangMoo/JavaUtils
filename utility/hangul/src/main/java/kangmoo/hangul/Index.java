package kangmoo.hangul;

//export function excludeLastElement(array: string[]): [string[], string] {
//        const lastElement = array[array.length - 1];
//        return [array.slice(0, -1), lastElement ?? ''];
//        }
//
//export function joinString(...args: string[]) {
//    return args.join('');
//}
//
//export function isBlank(character: string) {
//    return /^\s$/.test(character);
//}
//
//export default function assert(condition: boolean, errorMessage?: string): asserts condition {
//        if (condition === false) {
//        throw new Error(errorMessage ?? 'Invalid condition');
//  }
//          }


import static kangmoo.hangul.Disassemble.disassembleHangul;

public class Index {
    static Pair<String, String> excludeLastElement(String array) {
        if (array.length() == 1) {
            array = String.join("", disassembleHangul(array));
        }
        return new Pair<>(array.substring(0, array.length() - 1), array.substring(array.length() - 1));
    }

//    public static List<String> excludeLastElement(List<String> array) {
//        List<String> result = new ArrayList<>(array.subList(0, array.size() - 1));
//        String lastElement = array.size() > 0 ? array.get(array.size() - 1) : "";
//        result.add(lastElement);
//        return result;
//    }
//    public static List<String> excludeLastElement(String... array) {
//        return excludeLastElement(Arrays.asList(array));
//    }

//    static Pair<List<String>, String> excludeLastElement(List<String> array) {
//        if (array == null || array.isEmpty()) {
//            throw new IllegalArgumentException("Array cannot be null or empty");
//        }
//
//        int lastIndex = array.size() - 1;
//        String lastElement = array.get(lastIndex);
//        List<String> newArray = new ArrayList<>(array.subList(0, lastIndex));
//
//        return new Pair<>(newArray, lastElement);
//    }
//    static Pair<List<String>, String> excludeLastElement(String... array) {
//        return excludeLastElement(Arrays.asList(array));
//    }

    public static String joinString(String... args) {
        return String.join("", args);
    }

    public static boolean isBlank(char character) {
        return Character.isWhitespace(character);
    }

    public static void assertCondition(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage != null ? errorMessage : "Invalid condition");
        }
    }
}
