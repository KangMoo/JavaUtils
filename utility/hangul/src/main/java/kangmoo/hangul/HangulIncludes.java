package kangmoo.hangul;

import static kangmoo.hangul.Disassemble.disassembleHangul;

public class HangulIncludes {
    public static boolean hangulIncludes(String x, String y) {
        String disassembledX = disassembleHangul(x);
        String disassembledY = disassembleHangul(y);

        return disassembledX.contains(disassembledY);
    }
}
