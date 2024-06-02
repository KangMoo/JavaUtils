package kangmoo.hangul;


import java.util.Arrays;
import java.util.List;

import static kangmoo.hangul.Assemble.assembleHangul;
import static kangmoo.hangul.Disassemble.disassembleHangul;
import static kangmoo.hangul.Disassemble.disassembleHangulToGroups;
import static kangmoo.hangul.Index.excludeLastElement;

public class RemoveLastHangulCharacter {
    public static String removeLastHangulCharacter(String words) {
        if (words.isEmpty()) return words;
        if (words.length() == 1) {
            String[] split = disassembleHangul(words).split("");
            return assembleHangul(Arrays.copyOf(split, split.length - 1));
        }

        List<String> disassembledGroups = disassembleHangulToGroups(words);
        String lastCharacter = disassembledGroups.remove(disassembledGroups.size() - 1);
        String withoutLastCharacter = disassembledGroups.stream().map(Assemble::assembleHangul).reduce(String::concat).orElse("");
        return withoutLastCharacter + assembleHangul(excludeLastElement(lastCharacter).first());
    }
}
