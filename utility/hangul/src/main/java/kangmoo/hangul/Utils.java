package kangmoo.hangul;

import java.util.List;
import java.util.Map;

import static kangmoo.hangul.Constants.*;

public class Utils {

    public static boolean hasBatchim(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        char lastChar = str.charAt(str.length() - 1);
        Map<String, String> disassembled = DisassembleCompleteHangulCharacter.disassembleCompleteHangulCharacter(lastChar);

        return disassembled != null && !disassembled.get("last").isEmpty();
    }

    public static boolean hasSingleBatchim(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        char lastChar = str.charAt(str.length() - 1);

        if (!hasBatchim(String.valueOf(lastChar))) {
            return false;
        }

        String disassembled = Disassemble.disassembleHangul(String.valueOf(lastChar));
        return disassembled.length() == 3;
    }

    public static String getChosung(String word) {
        List<String> groups = Disassemble.disassembleHangulToGroups(word);
        StringBuilder chosung = new StringBuilder();
        for (String group : groups) {
            chosung.append(group.charAt(0));
        }
        return chosung.toString();
    }

    @Deprecated
    public static String getFirstConsonants(String word) {
        return getChosung(word);
    }

    public static boolean canBeChosung(char character) {
        return canBeChosung(String.valueOf(character));
    }

    public static boolean canBeChosung(String character) {
        return hasValueInReadOnlyStringList(HANGUL_CHARACTERS_BY_FIRST_INDEX, character);
    }


    public static boolean canBeJungsung(char character) {
        return canBeJungsung(String.valueOf(character));
    }

    public static boolean canBeJungsung(String character) {
        return hasValueInReadOnlyStringList(HANGUL_CHARACTERS_BY_MIDDLE_INDEX, character);
    }

    public static boolean canBeJongsung(char character) {
        return canBeJongsung(String.valueOf(character));
    }

    public static boolean canBeJongsung(String character) {
        return hasValueInReadOnlyStringList(HANGUL_CHARACTERS_BY_LAST_INDEX, character);
    }

    public static boolean hasProperty(Object obj, String key) {
        return obj instanceof Map && ((Map<?, ?>) obj).containsKey(key);
    }

    public static boolean hasValueInReadOnlyStringList(List<String> list, String value) {
        for (String item : list) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}