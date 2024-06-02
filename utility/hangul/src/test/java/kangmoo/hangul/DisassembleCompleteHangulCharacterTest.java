package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static kangmoo.hangul.DisassembleCompleteHangulCharacter.disassembleCompleteHangulCharacter;
import static org.assertj.core.api.Assertions.assertThat;

class DisassembleCompleteHangulCharacterTest {

    @Test
    void disassembleCompleteHangulCharacterTest() {
        assertThat(disassembleCompleteHangulCharacter("값")).isEqualTo(Map.of(
                "first", "ㄱ",
                "middle", "ㅏ",
                "last", "ㅂㅅ"
        ));

        assertThat(disassembleCompleteHangulCharacter("리")).isEqualTo(Map.of(
                "first", "ㄹ",
                "middle", "ㅣ",
                "last", ""
        ));

        assertThat(disassembleCompleteHangulCharacter("빚")).isEqualTo(Map.of(
                "first", "ㅂ",
                "middle", "ㅣ",
                "last", "ㅈ"
        ));

        assertThat(disassembleCompleteHangulCharacter("박")).isEqualTo(Map.of(
                "first", "ㅂ",
                "middle", "ㅏ",
                "last", "ㄱ"
        ));
    }
}