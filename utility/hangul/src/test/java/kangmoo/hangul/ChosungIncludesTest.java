package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.ChosungIncludes.chosungIncludes;
import static org.assertj.core.api.Assertions.assertThat;

class ChosungIncludesTest {

    @Test
    void chosungIncludesTest() {

        // 초성이 포함되어있다고 판단되는 경우

        //"ㅍㄹㅌ" 문자열로 "프론트엔드"를 검색하면 true를 반환한다.
        assertThat(chosungIncludes("프론트엔드", "ㅍㄹㅌ")).isTrue();
        //"ㅍㄹㅌ" 문자열로 "00프론트엔드"를 검색하면 true를 반환한다.
        assertThat(chosungIncludes("00프론트엔드", "ㅍㄹㅌ")).isTrue();
        //"ㅍㄹㅌㅇㄷㄱㅂㅈ" 문자열로 "프론트엔드 개발자"를 검색하면 true를 반환한다.
        assertThat(chosungIncludes("프론트엔드 개발자", "ㅍㄹㅌㅇㄷㄱㅂㅈ")).isTrue();
        //"ㅍㄹㅌㅇㄷ ㄱㅂㅈ" 문자열로 "프론트엔드 개발자"를 검색하면 true를 반환한다.
        assertThat(chosungIncludes("프론트엔드 개발자", "ㅍㄹㅌㅇㄷ ㄱㅂㅈ")).isTrue();

        // 초성이 포함되어있다고 판단되는 경우

        //"ㅍㅌ" 문자열로 "프론트엔드"를 검색하면 false를 반환한다.
        assertThat(chosungIncludes("프론트엔드", "ㅍㅌ")).isFalse();
        //"빈 문자열로 "프론트엔드 개발자"를 검색하면 false를 반환한다.
        assertThat(chosungIncludes("프론트엔드 개발자", " ")).isFalse();
        //"푸롴트" 문자열로 "프론트엔드"를 검색하면 초성으로만 구성되어 있지 않아 false를 반환한다.
        assertThat(chosungIncludes("프론트엔드", "푸롴트")).isFalse();
    }
}