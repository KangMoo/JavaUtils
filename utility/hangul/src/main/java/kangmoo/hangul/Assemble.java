package kangmoo.hangul;


import java.util.Arrays;

public class Assemble {
    public static String assembleHangul(String... words) {
        var disassembled = Disassemble.disassembleHangul(String.join("", words)).split("");
        return Arrays.stream(disassembled)
                .reduce(Hangul::binaryAssembleHangul).orElse("");
    }
}
