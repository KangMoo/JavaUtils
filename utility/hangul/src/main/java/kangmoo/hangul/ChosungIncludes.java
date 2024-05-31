package kangmoo.hangul;
import java.util.Arrays;

public class ChosungIncludes {

    public static boolean chosungIncludes(String x, String y) {
        String trimmedY = y.replaceAll("\\s", "");

        if (!isOnlyChosung(trimmedY)) {
            return false;
        }

        String chosungX = Utils.getChosung(x).replaceAll("\\s", "");
        String chosungY = trimmedY;

        return chosungX.contains(chosungY);
    }

    private static boolean isOnlyChosung(String str) {
        String[] groups = Disassemble.disassembleHangulToGroups(str).toArray(new String[0]);
        if (groups.length == 0) {
            return false;
        }

        return Arrays.stream(groups).allMatch(disassembled -> disassembled.length() == 1 && Utils.canBeChosung(disassembled));
    }
}
