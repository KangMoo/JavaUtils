package kangmoo.hangul;

import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kangmoo.hangul.Disassemble.disassembleHangul;
import static kangmoo.hangul.Disassemble.disassembleHangulToGroups;
import static org.assertj.core.api.Assertions.assertThat;

class DisassembleTest {

    @Test
    void disassembleHangulToGroupsTest() {
        assertThat(disassembleHangulToGroups("값")).isEqualTo(List.of("ㄱㅏㅂㅅ"));
        assertThat(disassembleHangulToGroups("값이 비싸다")).isEqualTo(List.of("ㄱㅏㅂㅅ", "ㅇㅣ", " ", "ㅂㅣ", "ㅆㅏ", "ㄷㅏ"));
        assertThat(disassembleHangulToGroups("사과 짱")).isEqualTo(List.of("ㅅㅏ", "ㄱㅗㅏ", " ", "ㅉㅏㅇ"));
        assertThat(disassembleHangulToGroups("ㄵ")).isEqualTo(List.of("ㄴㅈ"));
        assertThat(disassembleHangulToGroups("ㅘ")).isEqualTo(List.of("ㅗㅏ"));
    }

    @Test
    void disassembleHangulTest() {
        assertThat(disassembleHangul("값")).isEqualTo("ㄱㅏㅂㅅ");
        assertThat(disassembleHangul("값이 비싸다")).isEqualTo("ㄱㅏㅂㅅㅇㅣ ㅂㅣㅆㅏㄷㅏ");
        assertThat(disassembleHangul("사과 짱")).isEqualTo("ㅅㅏㄱㅗㅏ ㅉㅏㅇ");
        assertThat(disassembleHangul("ㄵ")).isEqualTo("ㄴㅈ");
        assertThat(disassembleHangul("ㅘ")).isEqualTo("ㅗㅏ");
    }
}