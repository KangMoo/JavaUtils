package kangmoo.hangul;

import static kangmoo.hangul.Constants.QWERTY_KEYBOARD_MAP;

public class ConvertQwertyToHangulAlphabet {
    public static String convertQwertyToHangulAlphabet(String word) {
        return word
                .chars()
                .mapToObj(inputText -> QWERTY_KEYBOARD_MAP.getOrDefault(String.valueOf((char) inputText), String.valueOf((char) inputText)))
                .reduce(String::concat)
                .orElse("");
    }

    // TODO
//    public static String convertQwertyToHangul(String word) {
//        if (word == null) {
//            return "";
//        }
//        return Assemble.assembleHangul(convertQwertyToHangulAlphabet(word).split(""));
//    }
}
