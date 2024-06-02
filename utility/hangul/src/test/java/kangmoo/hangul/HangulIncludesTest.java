package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.HangulIncludes.hangulIncludes;
import static org.assertj.core.api.Assertions.assertThat;

class HangulIncludesTest {

    @Test
    void hangulIncludesTest() {
        // 한글이 포함되어있다고 판단되는 경우
        assertThat(hangulIncludes("사과", "")).isTrue();
        assertThat(hangulIncludes("사과", "ㅅ")).isTrue();
        assertThat(hangulIncludes("사과", "삭")).isTrue();
        assertThat(hangulIncludes("사과", "사과")).isTrue();
        assertThat(hangulIncludes("프론트엔드", "")).isTrue();
        assertThat(hangulIncludes("프론트엔드", "플")).isTrue();
        assertThat(hangulIncludes("프론트엔드", "틍")).isTrue();
        assertThat(hangulIncludes("프론트엔드", "플")).isTrue();
        assertThat(hangulIncludes("프론트엔드", "프로")).isTrue();

        // 한글이 포함되어있다고 판단되지 않는 경우
        assertThat(hangulIncludes("사과", "삽")).isFalse();
        assertThat(hangulIncludes("프론트엔드", "픏")).isFalse();
    }
}