package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.ConvertQwertyToHangulAlphabet.convertQwertyToHangul;
import static kangmoo.hangul.ConvertQwertyToHangulAlphabet.convertQwertyToHangulAlphabet;
import static org.assertj.core.api.Assertions.assertThat;

class ConvertQwertyToHangulAlphabetTest {

    @Test
    void convertQwertyToHangulAlphabetTest() {
        //영어 알파벳을 한글 음소로 바꾼다.
        assertThat(convertQwertyToHangulAlphabet("abc")).isEqualTo("ㅁㅠㅊ");
        //쌍/자모음에 대응하지 않는 영어 알파벳을 한글 음소로 바꾼다.
        assertThat(convertQwertyToHangulAlphabet("ABC")).isEqualTo("ㅁㅠㅊ");
        //영어 알파벳은 한글 음소로 바꾸고, 한글 음절은 유지한다.
        assertThat(convertQwertyToHangulAlphabet("vm론트")).isEqualTo("ㅍㅡ론트");
        //분해된 한글 음소는 유지한다.
        assertThat(convertQwertyToHangulAlphabet("ㅍㅡㄹㅗㄴㅌㅡ")).isEqualTo("ㅍㅡㄹㅗㄴㅌㅡ");
        //영어 알파벳이 아닌 입력은 유지한다.
        assertThat(convertQwertyToHangulAlphabet("4월/20dlf!")).isEqualTo("4월/20ㅇㅣㄹ!");
        //영문 대문자는 쌍자/모음으로 바꾼다.
        assertThat(convertQwertyToHangulAlphabet("RㅏㄱEㅜrl")).isEqualTo("ㄲㅏㄱㄸㅜㄱㅣ");
        assertThat(convertQwertyToHangulAlphabet("ㅇPdml")).isEqualTo("ㅇㅖㅇㅡㅣ");
        //빈 문자열은 빈 문자열을 반환한다.
        assertThat(convertQwertyToHangulAlphabet("")).isEqualTo("");
    }

    @Test
    void convertQwertyToHangulTest() {
        //영어 알파벳을 한글로 합성한다.
        assertThat(convertQwertyToHangul("abc")).isEqualTo("뮻");
        assertThat(convertQwertyToHangul("vmfhsxmdpsem")).isEqualTo("프론트엔드");
        //쌍/자모음에 대응하지 않는 영어 알파벳을 한글로 합성한다.
        assertThat(convertQwertyToHangul("ABC")).isEqualTo("뮻");
        assertThat(convertQwertyToHangul("VMFHSXM")).isEqualTo("프론트");
        //영어 알파벳은 한글로 합성하고, 한글 음절은 유지한다.
        assertThat(convertQwertyToHangul("vm론트")).isEqualTo("프론트");
        //인자가 한글 음소면 한글로 합성한다.
        assertThat(convertQwertyToHangul("ㅍㅡㄹㅗㄴㅌㅡ")).isEqualTo("프론트");
        //영문 대문자는 쌍자/모음에 대응하며 한글로 합성한다.
        assertThat(convertQwertyToHangul("RㅏㄱEㅜrl")).isEqualTo("깍뚜기");
        assertThat(convertQwertyToHangul("ㅇPdml")).isEqualTo("예의");
        //빈 문자열은 빈 문자열을 반환한다.
        assertThat(convertQwertyToHangul("")).isEqualTo("");
    }
}