package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.Utils.*;
import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @Test
    void hasBatchimTest() {
        // 받침이 있다고 판단되는 경우
        assertThat(hasBatchim("값")).isTrue();
        assertThat(hasBatchim("공")).isTrue();
        assertThat(hasBatchim("읊")).isTrue();

        // 받침이 없다고 판단되는 경우
        assertThat(hasBatchim("토")).isFalse();
        assertThat(hasBatchim("서")).isFalse();
    }

    @Test
    void hasSingleBatchimTest() {
        // 홑받침이라고 판단되는 경우
        assertThat(hasSingleBatchim("공")).isTrue();
        assertThat(hasSingleBatchim("핫")).isTrue();
        assertThat(hasSingleBatchim("양")).isTrue();
        assertThat(hasSingleBatchim("신")).isTrue();

        // 겹받침이라고 판단되는 경우
        assertThat(hasSingleBatchim("값")).isFalse();
        assertThat(hasSingleBatchim("읊")).isFalse();

        // 받침이 없다고 판단되는 경우
        assertThat(hasSingleBatchim("토")).isFalse();
        assertThat(hasSingleBatchim("서")).isFalse();
    }

    @Test
    void getChosungTest() {
        assertThat(getChosung("사과")).isEqualTo("ㅅㄱ");
        assertThat(getChosung("프론트엔드")).isEqualTo("ㅍㄹㅌㅇㄷ");
        assertThat(getChosung("ㄴㅈ")).isEqualTo("ㄴㅈ");
        assertThat(getChosung("리액트")).isEqualTo("ㄹㅇㅌ");
        assertThat(getChosung("띄어 쓰기")).isEqualTo("ㄸㅇ ㅆㄱ");
    }

    @Test
    void canBeChosungTest() {
        // 초성이 될 수 있다고 판단되는 경우
        assertThat(canBeChosung("ㄱ")).isTrue();
        assertThat(canBeChosung("ㅃ")).isTrue();

        // 초성이 될 수 없다고 판단되는 경우
        assertThat(canBeChosung("ㅏ")).isFalse();
        assertThat(canBeChosung("ㅘ")).isFalse();
        assertThat(canBeChosung("ㄱㅅ")).isFalse();
        assertThat(canBeChosung("가")).isFalse();
    }

    @Test
    void canBeJungsungTest() {
        // 중성이 될 수 있다고 판단되는 경우
        assertThat(canBeJungsung("ㅗㅏ")).isTrue();
        assertThat(canBeJungsung("ㅏ")).isTrue();

        // 중성이 될 수 없다고 판단되는 경우
        assertThat(canBeJungsung("ㄱ")).isFalse();
        assertThat(canBeJungsung("ㄱㅅ")).isFalse();
        assertThat(canBeJungsung("가")).isFalse();
    }

    @Test
    void canBeJongsungTest() {
        // 종성이 될 수 있다고 판단되는 경우
        assertThat(canBeJongsung("ㄱ")).isTrue();
        assertThat(canBeJongsung("ㄱㅅ")).isTrue();
        assertThat(canBeJongsung("ㅂㅅ")).isTrue();

        // 종성이 될 수 없다고 판단되는 경우
        assertThat(canBeJongsung("ㅎㄹ")).isFalse();
        assertThat(canBeJongsung("ㅗㅏ")).isFalse();
        assertThat(canBeJongsung("ㅏ")).isFalse();
        assertThat(canBeJongsung("ㅏ")).isFalse();
    }
}