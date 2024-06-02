package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.Hangul.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HangulTest {

    @Test
    void isHangulCharacterTest() {
        // isHangulCharacter는 완성된 한글 문자를 받으면 true를 반환한다.
        assertThat(isHangulCharacter("가")).isTrue();
        assertThat(isHangulCharacter("값")).isTrue();
        assertThat(isHangulCharacter("ㄱ")).isFalse();
        assertThat(isHangulCharacter("ㅏ")).isFalse();
        assertThat(isHangulCharacter("a")).isFalse();
    }

    @Test
    void isHangulAlphabetTest() {
        // isHangulAlphabet은 조합되지않은 한글 문자를 받으면 true를 반환한다
        assertThat(isHangulAlphabet("가")).isFalse();
        assertThat(isHangulAlphabet("값")).isFalse();
        assertThat(isHangulAlphabet("ㄱ")).isTrue();
        assertThat(isHangulAlphabet("ㅏ")).isTrue();
        assertThat(isHangulAlphabet("a")).isFalse();
    }

    @Test
    void binaryAssembleHangulAlphabetsTest() {
        assertThat(binaryAssembleHangulAlphabets("ㄱ", "ㅏ")).isEqualTo("가");
        assertThat(binaryAssembleHangulAlphabets("ㅗ", "ㅏ")).isEqualTo("ㅘ");
    }

    @Test
    void linkHangulCharactersTest() {
    }

    @Test
    void binaryAssembleHangulCharactersTest() {
        // 초성과 중성만 조합
        assertThat(binaryAssembleHangulCharacters("ㄱ", "ㅏ")).isEqualTo("가");

        // 초성과 중성이 합쳐진 문자와 종성을 조합
        assertThat(binaryAssembleHangulCharacters("가", "ㅇ")).isEqualTo("강");

        // 초성과 중성과 종성이 합쳐진 문자와 자음을 조합하여 겹받침 만들기
        assertThat(binaryAssembleHangulCharacters("갑", "ㅅ")).isEqualTo("값");

        // 초성과 중성이 합쳐진 문자와 모음을 조립하여 겹모음 만들기
        assertThat(binaryAssembleHangulCharacters("고", "ㅏ")).isEqualTo("과");

        // 모음만 있는 문자와 모음을 조합하여 겹모음 만들기
        assertThat(binaryAssembleHangulCharacters("ㅗ", "ㅏ")).isEqualTo("ㅘ");

        // 초성과 중성과 종성이 합쳐진 문자의 연음 법칙
        assertThat(binaryAssembleHangulCharacters("톳", "ㅡ")).isEqualTo("토스");

        // 초성과 중성과 종성(겹받침)이 합쳐진 문자의 연음 법칙
        assertThat(binaryAssembleHangulCharacters("닭", "ㅏ")).isEqualTo("달가");
        assertThat(binaryAssembleHangulCharacters("깎", "ㅏ")).isEqualTo("까까");

        // 문법에 맞지 않는 문자를 조합하면 단순 Join 한다. (문법 순서 틀림)
        assertThat(binaryAssembleHangulCharacters("ㅏ", "ㄱ")).isEqualTo("ㅏㄱ");
        assertThat(binaryAssembleHangulCharacters("까", "ㅃ")).isEqualTo("까ㅃ");
        assertThat(binaryAssembleHangulCharacters("ㅘ", "ㅏ")).isEqualTo("ㅘㅏ");

        // 순서대로 입력했을 때 조합이 불가능한 문자라면 단순 Join 한다.
        assertThat(binaryAssembleHangulCharacters("뼈", "ㅣ")).isEqualTo("뼈ㅣ");

        // 소스가 두 글자 이상이라면 Invalid source 에러를 발생시킨다.
        assertThat(assertThrows(IllegalArgumentException.class, () -> binaryAssembleHangulCharacters("가나", "ㄴ")))
                .hasMessage("Invalid source character: 가나. Source must be one character.");
        assertThat(assertThrows(IllegalArgumentException.class, () -> binaryAssembleHangulCharacters("ㄱㄴ", "ㅏ")))
                .hasMessage("Invalid source character: ㄱㄴ. Source must be one character.");

        // 다음 문자가 한글 문자 한 글자가 아니라면 Invalid next character 에러를 발생시킨다.
        assertThat(assertThrows(IllegalArgumentException.class, () -> binaryAssembleHangulCharacters("ㄱ", "a")))
                .hasMessage("Invalid next character: a. Next character must be one of the chosung, jungsung, or jongsung.");
        assertThat(assertThrows(IllegalArgumentException.class, () -> binaryAssembleHangulCharacters("ㄱ", "ㅡㅏ")))
                .hasMessage("Invalid next character: ㅡㅏ. Next character must be one of the chosung, jungsung, or jongsung.");
    }

    @Test
    void binaryAssembleHangulTest() {
        assertThat(binaryAssembleHangul("저는 고양이를 좋아합닏", "ㅏ")).isEqualTo("저는 고양이를 좋아합니다");
        assertThat(binaryAssembleHangul("저는 고양이를 좋아하", "ㅂ")).isEqualTo("저는 고양이를 좋아합");
        assertThat(binaryAssembleHangul("저는 고양이를 좋아합", "ㅅ")).isEqualTo("저는 고양이를 좋아핪");
        assertThat(binaryAssembleHangul("저는 고양이를 좋아합", "ㄲ")).isEqualTo("저는 고양이를 좋아합ㄲ");
        assertThat(binaryAssembleHangul("저는 고양이를 좋아합", "ㅂ")).isEqualTo("저는 고양이를 좋아합ㅂ");
        assertThat(binaryAssembleHangul("저는 고양이를 좋아하", "ㅏ")).isEqualTo("저는 고양이를 좋아하ㅏ");
        assertThat(binaryAssembleHangul("저는 고양이를 좋아합니다", "ㅜ")).isEqualTo("저는 고양이를 좋아합니다ㅜ");
    }
}