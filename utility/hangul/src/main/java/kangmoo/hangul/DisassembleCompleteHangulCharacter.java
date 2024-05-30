package kangmoo.hangul;

import java.util.HashMap;
import java.util.Map;

public class DisassembleCompleteHangulCharacter {
    public static Map<String, String> disassembleCompleteHangulCharacter(char character) {
        if ((int) character < Constants.COMPLETE_HANGUL_START_CHARCODE || (int) character > Constants.COMPLETE_HANGUL_END_CHARCODE) {
            return null;
        }

        int index = (int) character - Constants.COMPLETE_HANGUL_START_CHARCODE;
        int firstIndex = index / (Constants.NUMBER_OF_JUNGSUNG * Constants.NUMBER_OF_JONGSUNG);
        int middleIndex = (index % (Constants.NUMBER_OF_JUNGSUNG * Constants.NUMBER_OF_JONGSUNG)) / Constants.NUMBER_OF_JONGSUNG;
        int lastIndex = index % Constants.NUMBER_OF_JONGSUNG;

        Map<String, String> result = new HashMap<>();
        result.put("first", Constants.HANGUL_CHARACTERS_BY_FIRST_INDEX.get(firstIndex));
        result.put("middle", Constants.HANGUL_CHARACTERS_BY_MIDDLE_INDEX.get(middleIndex));
        result.put("last", Constants.HANGUL_CHARACTERS_BY_LAST_INDEX.get(lastIndex));

        return result;
    }
}
