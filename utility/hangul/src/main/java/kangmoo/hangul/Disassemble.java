package kangmoo.hangul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static kangmoo.hangul.DisassembleCompleteHangulCharacter.disassembleCompleteHangulCharacter;

public class Disassemble {
    public static List<String> disassembleHangulToGroups(String str) {
        List<String> result = new ArrayList<>();

        for (char letter : str.toCharArray()) {
            Map<String, String> disassembledComplete = disassembleCompleteHangulCharacter(letter);

            if (disassembledComplete != null) {
                result.add(disassembledComplete.get("first") + disassembledComplete.get("middle") + disassembledComplete.get("last"));
                continue;
            }

            if (Constants.DISASSEMBLED_CONSONANTS_BY_CONSONANT.containsKey(String.valueOf(letter))) {
                result.add(Constants.DISASSEMBLED_CONSONANTS_BY_CONSONANT.get(String.valueOf(letter)));
                continue;
            }

            if (Constants.DISASSEMBLED_VOWELS_BY_VOWEL.containsKey(String.valueOf(letter))) {
                result.add(Constants.DISASSEMBLED_VOWELS_BY_VOWEL.get(String.valueOf(letter)));
                continue;
            }

            result.add(String.valueOf(letter));
        }

        return result;
    }

    public static String disassembleHangul(String str) {
        StringBuilder result = new StringBuilder();
        for (String group : disassembleHangulToGroups(str)) {
            result.append(group);
        }
        return result.toString();
    }
}
