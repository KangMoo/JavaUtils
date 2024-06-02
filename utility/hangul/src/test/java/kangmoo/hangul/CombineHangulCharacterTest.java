package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.CombineHangulCharacter.combineHangulCharacter;
import static kangmoo.hangul.CombineHangulCharacter.combineVowels;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CombineHangulCharacterTest {

    @Test
    void combineHangulCharacterTest() {
        assertThat(combineHangulCharacter("ㄱ", "ㅏ", "ㅂㅅ")).isEqualTo("값");
        assertThat(combineHangulCharacter("ㅌ", "ㅗ")).isEqualTo("토");
        assertThat(combineHangulCharacter("ㅌ", "ㅗ", "ㅅ")).isEqualTo("톳");

        // 초성이 될 수 없는 문자가 초성으로 입력되면 에러를 반환한다. (ㅏ, ㅏ, ㄱ)
        assertThat(assertThrows(IllegalArgumentException.class, () -> combineHangulCharacter("ㅏ", "ㅏ", "ㄱ")))
                .hasMessage("Invalid hangul Characters: ㅏ, ㅏ, ㄱ");
        // 중성이 될 수 없는 문자가 중성으로 입력되면 에러를 반환한다. (ㄱ, ㄴ, ㅃ)
        assertThat(assertThrows(IllegalArgumentException.class, () -> combineHangulCharacter("ㄱ", "ㄴ", "ㅃ")))
                .hasMessage("Invalid hangul Characters: ㄱ, ㄴ, ㅃ");
        // 종성이 될 수 없는 문자가 종성으로 입력되면 에러를 반환한다. (ㄱ, ㅏ, ㅃ)
        assertThat(assertThrows(IllegalArgumentException.class, () -> combineHangulCharacter("ㄱ", "ㅏ", "ㅃ")))
                .hasMessage("Invalid hangul Characters: ㄱ, ㅏ, ㅃ");
        // 온전한 한글 문자가 하나라도 입력되면 에러를 반환한다. (가, ㅏ, ㄱ)
        assertThat(assertThrows(IllegalArgumentException.class, () -> combineHangulCharacter("가", "ㅏ", "ㄱ")))
                .hasMessage("Invalid hangul Characters: 가, ㅏ, ㄱ");
    }

    @Test
    void combineVowelsTest() {
        assertThat(combineVowels("ㅗ", "ㅏ")).isEqualTo("ㅘ");
        assertThat(combineVowels("ㅜ", "ㅔ")).isEqualTo("ㅞ");
        assertThat(combineVowels("ㅡ", "ㅣ")).isEqualTo("ㅢ");
        assertThat(combineVowels("ㅏ", "ㅗ")).isEqualTo("ㅏㅗ");
        assertThat(combineVowels("ㅣ", "ㅡ")).isEqualTo("ㅣㅡ");
        assertThat(combineVowels("ㅘ", "ㅏ")).isEqualTo("ㅘㅏ");
        assertThat(combineVowels("ㅝ", "ㅣ")).isEqualTo("ㅝㅣ");
    }
}