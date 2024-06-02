package kangmoo.hangul;

import org.junit.jupiter.api.Test;

import static kangmoo.hangul.Josa.josa;
import static org.assertj.core.api.Assertions.assertThat;

class JosaTest {

    @Test
    void josaTest() {
        // 주격조사
        assertThat(josa("샴푸", "이/가")).isEqualTo("샴푸가");
        assertThat(josa("칫솔", "이/가")).isEqualTo("칫솔이");
        // 목적격조사
        assertThat(josa("샴푸", "을/를")).isEqualTo("샴푸를");
        assertThat(josa("칫솔", "을/를")).isEqualTo("칫솔을");
        // 대조의 보조사
        assertThat(josa("샴푸", "은/는")).isEqualTo("샴푸는");
        assertThat(josa("칫솔", "은/는")).isEqualTo("칫솔은");
        // 방향의 격조사
        assertThat(josa("바깥", "으로/로")).isEqualTo("바깥으로");
        assertThat(josa("내부", "으로/로")).isEqualTo("내부로");
        // 방향의 격조사 ㄹ 받침 예외 처리
        assertThat(josa("지름길", "으로/로")).isEqualTo("지름길로");
        // 비교의 격조사
        assertThat(josa("샴푸", "와/과")).isEqualTo("샴푸와");
        assertThat(josa("칫솔", "와/과")).isEqualTo("칫솔과");
        // 선택의 보조사
        assertThat(josa("샴푸", "이나/나")).isEqualTo("샴푸나");
        assertThat(josa("칫솔", "이나/나")).isEqualTo("칫솔이나");
        // 화제의 보조사
        assertThat(josa("샴푸", "이란/란")).isEqualTo("샴푸란");
        assertThat(josa("칫솔", "이란/란")).isEqualTo("칫솔이란");
        // 호격조사
        assertThat(josa("철수", "아/야")).isEqualTo("철수야");
        assertThat(josa("길동", "아/야")).isEqualTo("길동아");
        // 접속조사
        assertThat(josa("고기", "이랑/랑")).isEqualTo("고기랑");
        assertThat(josa("과일", "이랑/랑")).isEqualTo("과일이랑");
        // 서술격조사와 종결어미
        assertThat(josa("사과", "이에요/예요")).isEqualTo("사과예요");
        assertThat(josa("책", "이에요/예요")).isEqualTo("책이에요");
        // 서술격조사와 종결어미, "이" 로 끝나는 단어 예외 처리
        assertThat(josa("때밀이", "이에요/예요")).isEqualTo("때밀이예요");
        // 지위나 신분 또는 자격을 나타내는 위격조사
        assertThat(josa("학생", "으로서/로서")).isEqualTo("학생으로서");
        assertThat(josa("부모", "으로서/로서")).isEqualTo("부모로서");
        // 지위나 신분 또는 자격을 나타내는 위격조사 ㄹ 받침 예외 처리
        assertThat(josa("라이벌", "으로서/로서")).isEqualTo("라이벌로서");
        // 수단의 의미를 나타내는 도구격조사
        assertThat(josa("토큰", "으로써/로써")).isEqualTo("토큰으로써");
        assertThat(josa("함수", "으로써/로써")).isEqualTo("함수로써");
        // 수단의 의미를 나타내는 도구격조사 ㄹ 받침 예외 처리
        assertThat(josa("건물", "으로써/로써")).isEqualTo("건물로써");
        // 어떤 행동의 출발점이나 비롯되는 대상임을 나타내는 격 조사
        assertThat(josa("역삼동", "으로부터/로부터")).isEqualTo("역삼동으로부터");
        assertThat(josa("저기", "으로부터/로부터")).isEqualTo("저기로부터");
        // 어떤 행동의 출발점이나 비롯되는 대상임을 나타내는 격 조사 ㄹ 받침 예외 처리
        assertThat(josa("동굴", "으로부터/로부터")).isEqualTo("동굴로부터");
        // 단어가 빈 문자열일 경우 빈 문자열을 반환한다.
        assertThat(josa("", "이/가")).isEqualTo("");
    }

    /*
      describe('josa.pick', () => {
    it('첫 번째 매개변수가 빈 문자열이라면 옵션 중 첫 번째 값을 반환한다.', () => {
      expect(josa.pick('', '이/가')).toBe('이');
    });
    it('주격조사', () => {
      expect(josa.pick('샴푸', '이/가')).toBe('가');
      expect(josa.pick('칫솔', '이/가')).toBe('이');
    });
    it('목적격조사', () => {
      expect(josa.pick('샴푸', '을/를')).toBe('를');
      expect(josa.pick('칫솔', '을/를')).toBe('을');
    });
    it('대조의 보조사', () => {
      expect(josa.pick('샴푸', '은/는')).toBe('는');
      expect(josa.pick('칫솔', '은/는')).toBe('은');
    });
    it('방향의 격조사', () => {
      expect(josa.pick('바깥', '으로/로')).toBe('으로');
      expect(josa.pick('내부', '으로/로')).toBe('로');
    });
    it('방향의 격조사 ㄹ 받침 예외 처리', () => {
      expect(josa.pick('지름길', '으로/로')).toBe('로');
    });
    it('비교의 격조사', () => {
      expect(josa.pick('샴푸', '와/과')).toBe('와');
      expect(josa.pick('칫솔', '와/과')).toBe('과');
    });
    it('선택의 보조사', () => {
      expect(josa.pick('샴푸', '이나/나')).toBe('나');
      expect(josa.pick('칫솔', '이나/나')).toBe('이나');
    });
    it('화제의 보조사', () => {
      expect(josa.pick('샴푸', '이란/란')).toBe('란');
      expect(josa.pick('칫솔', '이란/란')).toBe('이란');
    });
    it('호격조사', () => {
      expect(josa.pick('철수', '아/야')).toBe('야');
      expect(josa.pick('길동', '아/야')).toBe('아');
    });
    it('접속조사', () => {
      expect(josa.pick('고기', '이랑/랑')).toBe('랑');
      expect(josa.pick('과일', '이랑/랑')).toBe('이랑');
    });
    it('서술격조사와 종결어미', () => {
      expect(josa.pick('사과', '이에요/예요')).toBe('예요');
      expect(josa.pick('책', '이에요/예요')).toBe('이에요');
    });
    it('서술격조사와 종결어미, "이" 로 끝나는 단어 예외 처리', () => {
      expect(josa.pick('때밀이', '이에요/예요')).toBe('예요');
    });
    it('지위나 신분 또는 자격을 나타내는 위격조사', () => {
      expect(josa.pick('학생', '으로서/로서')).toBe('으로서');
      expect(josa.pick('부모', '으로서/로서')).toBe('로서');
    });
    it('지위나 신분 또는 자격을 나타내는 위격조사 ㄹ 받침 예외 처리', () => {
      expect(josa.pick('라이벌', '으로서/로서')).toBe('로서');
    });
    it('수단의 의미를 나타내는 도구격조사', () => {
      expect(josa.pick('토큰', '으로써/로써')).toBe('으로써');
      expect(josa.pick('함수', '으로써/로써')).toBe('로써');
    });
    it('수단의 의미를 나타내는 도구격조사 ㄹ 받침 예외 처리', () => {
      expect(josa.pick('건물', '으로써/로써')).toBe('로써');
    });
    it('어떤 행동의 출발점이나 비롯되는 대상임을 나타내는 격 조사', () => {
      expect(josa.pick('역삼동', '으로부터/로부터')).toBe('으로부터');
      expect(josa.pick('저기', '으로부터/로부터')).toBe('로부터');
    });
    it('어떤 행동의 출발점이나 비롯되는 대상임을 나타내는 격 조사 ㄹ 받침 예외 처리', () => {
      expect(josa.pick('동굴', '으로부터/로부터')).toBe('로부터');
    });
  });
     */
    @Test
    void pickTest() {
        // 첫 번째 매개변수가 빈 문자열이라면 옵션 중 첫 번째 값을 반환한다.
        assertThat(Josa.pick("", "이/가")).isEqualTo("이");
        // 주격조사
        assertThat(Josa.pick("샴푸", "이/가")).isEqualTo("가");
        assertThat(Josa.pick("칫솔", "이/가")).isEqualTo("이");
        // 목적격조사
        assertThat(Josa.pick("샴푸", "을/를")).isEqualTo("를");
        assertThat(Josa.pick("칫솔", "을/를")).isEqualTo("을");
        // 대조의 보조사
        assertThat(Josa.pick("샴푸", "은/는")).isEqualTo("는");
        assertThat(Josa.pick("칫솔", "은/는")).isEqualTo("은");
        // 방향의 격조사
        assertThat(Josa.pick("바깥", "으로/로")).isEqualTo("으로");
        assertThat(Josa.pick("내부", "으로/로")).isEqualTo("로");
        // 방향의 격조사 ㄹ 받침 예외 처리
        assertThat(Josa.pick("지름길", "으로/로")).isEqualTo("로");
        // 비교의 격조사
        assertThat(Josa.pick("샴푸", "와/과")).isEqualTo("와");
        assertThat(Josa.pick("칫솔", "와/과")).isEqualTo("과");
        // 선택의 보조사
        assertThat(Josa.pick("샴푸", "이나/나")).isEqualTo("나");
        assertThat(Josa.pick("칫솔", "이나/나")).isEqualTo("이나");
        // 화제의 보조사
        assertThat(Josa.pick("샴푸", "이란/란")).isEqualTo("란");
        assertThat(Josa.pick("칫솔", "이란/란")).isEqualTo("이란");
        // 호격조사
        assertThat(Josa.pick("철수", "아/야")).isEqualTo("야");
        assertThat(Josa.pick("길동", "아/야")).isEqualTo("아");
        // 접속조사
        assertThat(Josa.pick("고기", "이랑/랑")).isEqualTo("랑");
        assertThat(Josa.pick("과일", "이랑/랑")).isEqualTo("이랑");
        // 서술격조사와 종결어미
        assertThat(Josa.pick("사과", "이에요/예요")).isEqualTo("예요");
        assertThat(Josa.pick("책", "이에요/예요")).isEqualTo("이에요");
        // 서술격조사와 종결어미, "이" 로 끝나는 단어 예외 처리
        assertThat(Josa.pick("때밀이", "이에요/예요")).isEqualTo("예요");
        // 지위나 신분 또는 자격을 나타내는 위격조사
        assertThat(Josa.pick("학생", "으로서/로서")).isEqualTo("으로서");
        assertThat(Josa.pick("부모", "으로서/로서")).isEqualTo("로서");
        // 지위나 신분 또는 자격을 나타내는 위격조사 ㄹ 받침 예외 처리
        assertThat(Josa.pick("라이벌", "으로서/로서")).isEqualTo("로서");
        // 수단의 의미를 나타내는 도구격조사
        assertThat(Josa.pick("토큰", "으로써/로써")).isEqualTo("으로써");
        assertThat(Josa.pick("함수", "으로써/로써")).isEqualTo("로써");
        // 수단의 의미를 나타내는 도구격조사 ㄹ 받침 예외 처리
        assertThat(Josa.pick("건물", "으로써/로써")).isEqualTo("로써");
        // 어떤 행동의 출발점이나 비롯되는 대상임을 나타내는 격 조사
        assertThat(Josa.pick("역삼동", "으로부터/로부터")).isEqualTo("으로부터");
        assertThat(Josa.pick("저기", "으로부터/로부터")).isEqualTo("로부터");
        // 어떤 행동의 출발점이나 비롯되는 대상임을 나타내는 격 조사 ㄹ 받침 예외 처리
        assertThat(Josa.pick("동굴", "으로부터/로부터")).isEqualTo("로부터");
    }
}