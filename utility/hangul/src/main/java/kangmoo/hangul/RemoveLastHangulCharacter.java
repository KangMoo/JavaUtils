package kangmoo.hangul;


import java.util.Arrays;

import static kangmoo.hangul.CombineHangulCharacter.combineHangulCharacter;
import static kangmoo.hangul.Index.excludeLastElement;

public class RemoveLastHangulCharacter {
    public static String removeLastHangulCharacter(String words) {
        if (words.isEmpty()) return words;
        String[] split = words.split("");

        StringBuilder result = new StringBuilder();
        Arrays.stream(Arrays.copyOf(split, split.length - 1))
                .map(c -> c.charAt(0))
                .map(DisassembleCompleteHangulCharacter::disassembleCompleteHangulCharacter)
                .forEach(jamos -> {
                    if (jamos == null) {
                        result.append(split[result.length()]);
                        return;
                    }
                    result.append(combineHangulCharacter(jamos.get("first"), jamos.get("middle"), jamos.get("last")));
                });


        StringBuilder firstCharacter = new StringBuilder();
        StringBuilder middleCharacter = new StringBuilder();
        StringBuilder lastCharacter = new StringBuilder();

        excludeLastElement(split[split.length - 1]).first().chars().forEach(character -> {
            if (Utils.canBeJungsung((char) character)) {
                middleCharacter.append((char) character);
            } else {
                if (middleCharacter.isEmpty()) {
                    firstCharacter.append((char) character);
                } else {
                    lastCharacter.append((char) character);
                }
            }
        });

        result.append(middleCharacter.isEmpty() ? firstCharacter : combineHangulCharacter(firstCharacter.toString(), middleCharacter.toString(), lastCharacter.toString()));

        return result.toString();
    }
}
