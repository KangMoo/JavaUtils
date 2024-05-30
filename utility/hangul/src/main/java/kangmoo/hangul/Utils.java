package kangmoo.hangul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static List<String> getChosungArray(String word) {
        List<String> groups = Disassemble.disassembleHangulToGroups(word);
        List<String> chosungArray = new ArrayList<>();
        for (String group : groups) {
            chosungArray.add(String.valueOf(group.charAt(0)));
        }
        return chosungArray;
    }

    public static boolean isHangul(String str) {
        for (char c : str.toCharArray()) {
            if (c < Constants.COMPLETE_HANGUL_START_CHARCODE || c > Constants.COMPLETE_HANGUL_END_CHARCODE) {
                return false;
            }
        }
        return true;
    }
}
