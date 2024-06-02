package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.Assemble.assembleHangul;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AssembleTest {

    @Test
    void assembleHangulTest() {
        // 온전한 한글과 한글 문자 조합
        assertThat(assembleHangul("아버지가", " ", "방ㅇ", "ㅔ ", "들ㅇ", "ㅓ갑니다")).isEqualTo("아버지가 방에 들어갑니다");
        // 온전한 한글만 조합
        assertThat(assembleHangul("아버지가", " ", "방에 ", "들어갑니다")).isEqualTo("아버지가 방에 들어갑니다");
        // 온전하지 않은 한글만 조합
        assertThat(assembleHangul("ㅇ", "ㅏ", "ㅂ", "ㅓ", "ㅈ", "ㅣ")).isEqualTo("아버지");
    }
}