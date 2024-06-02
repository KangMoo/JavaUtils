package kangmoo.hangul;

import java.util.List;

import static kangmoo.hangul.DisassembleCompleteHangulCharacter.disassembleCompleteHangulCharacter;

public class Josa {
    static List<String> JosaOption = List.of("이/가", "을/를", "은/는", "으로/로", "와/과", "이나/나", "이란/란", "아/야", "이랑/랑", "이에요/예요", "으로서/로서", "으로써/로써", "으로부터/로부터");
    static List<String> 로_조사 = List.of("으로/로", "으로서/로서", "으로써/로써", "으로부터/로부터");

    public static String josa(String word, String josa) {
        if (word.isEmpty()) {
            return word;
        }

        return word + josaPicker(word, josa);
    }

    public static String pick(String word, String josa) {
        return josaPicker(word, josa);
    }

    private static String josaPicker(String word, String josa) {
        if (word.isEmpty()) {
            return josa.split("/")[0];
        }

        boolean hasBatchim = Utils.hasBatchim(word);
        int index = hasBatchim ? 0 : 1;

        boolean is종성ㄹ = disassembleCompleteHangulCharacter(word.charAt(word.length() - 1)).get("last").equals("ㄹ");

        boolean isCaseOf로 = hasBatchim && is종성ㄹ && 로_조사.contains(josa);

        if (josa.equals("와/과") || isCaseOf로) {
            index = index == 0 ? 1 : 0;
        }

        boolean isEndsWith이 = word.charAt(word.length() - 1) == '이';

        if (josa.equals("이에요/예요") && isEndsWith이) {
            index = 1;
        }

        return josa.split("/")[index];
    }
}
