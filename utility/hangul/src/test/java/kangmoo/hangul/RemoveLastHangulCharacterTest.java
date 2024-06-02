package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.RemoveLastHangulCharacter.removeLastHangulCharacter;
import static org.assertj.core.api.Assertions.assertThat;

class RemoveLastHangulCharacterTest {

    @Test
    void removeLastHangulCharacterTest() {
        assertThat(removeLastHangulCharacter("안녕하세요 값")).isEqualTo("안녕하세요 갑");
        assertThat(removeLastHangulCharacter("프론트엔드")).isEqualTo("프론트엔ㄷ");
        assertThat(removeLastHangulCharacter("일요일")).isEqualTo("일요이");
        assertThat(removeLastHangulCharacter("깎")).isEqualTo("까");
        assertThat(removeLastHangulCharacter("")).isEqualTo("");
    }
}