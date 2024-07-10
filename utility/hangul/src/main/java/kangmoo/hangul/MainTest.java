package kangmoo.hangul;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author kangmoo Heo
 */
public class MainTest {
    // 한글 유니코드 시작 위치
    private static final int HANGUL_BASE = 0xAC00;

    // 초성, 중성, 종성 개수
    private static final int INITIAL_COUNT = 19;
    private static final int MEDIAL_COUNT = 21;
    private static final int FINAL_COUNT = 28;

    public static void main(String[] args) {
        Set<String> 초성 = new TreeSet<>();
        Set<String> 중성 = new TreeSet<>();
        Set<String> 종성 = new TreeSet<>();
        int count = 0;
        for (int initial = 0; initial < INITIAL_COUNT; initial++) {
            for (int medial = 0; medial < MEDIAL_COUNT; medial++) {
                for (int finalChar = 0; finalChar < FINAL_COUNT; finalChar++) {
                    char hangulChar = (char) (HANGUL_BASE + (initial * MEDIAL_COUNT * FINAL_COUNT) + (medial * FINAL_COUNT) + finalChar);
                    System.out.print(hangulChar + " ");
//                    Map<String, String> stringStringMap = disassembleCompleteHangulCharacter(hangulChar);
                    Map<String, String> stringStringMap = disassemble(hangulChar);

                    초성.add(stringStringMap.get("first"));
                    중성.add(stringStringMap.get("middle"));
                    종성.add(stringStringMap.get("last"));
                    count++;
                }
                System.out.println(); // 중성이 바뀔 때마다 줄 바꿈
            }
        }

        System.out.println(count);

        System.out.println(초성);
        System.out.println(중성);
        System.out.println(종성);
    }

    /*public static void main(String[] args) {
        char hangulChar = '힣';  // 분리하고자 하는 한글 문자

        // 한글 유니코드 범위 확인
        if (hangulChar < '가' || hangulChar > '힣') {
            System.out.println("입력된 문자가 한글 음절 문자가 아닙니다.");
            return;
        }
//        System.out.println("TTTT");
        System.out.printf("%d\n", (int) 'ㄱ'); // 12593
        System.out.printf("%d\n", (int) 'ㅎ'); // 12622
        System.out.printf("%d\n", (int) 'ㅎ' - (int) 'ㄱ'); // 29
        System.out.printf("%d\n", (int) 'ㅏ'); // 12623
        System.out.printf("%d\n", (int) 'ㅣ'); // 12643
        System.out.printf("%d\n", (int) 'ㅣ' - (int) 'ㅏ'); // 20
        System.out.printf("%d\n", (int) '가'); // 44032
        System.out.printf("%d\n", (int) '힣'); // 55203
        System.out.printf("%d\n", (int) '힣'- (int) '가'); // 11171

        System.out.println((char) 0xAC00); // 가
        System.out.println((char) 0xD7A3); // 힣

        // 한글 유니코드 값에서 시작점(AC00)을 뺌
        int unicodeValue = hangulChar - '가';

        // 초성, 중성, 종성 인덱스 계산
        int cho = unicodeValue / (21 * 28);
        int jung = (unicodeValue % (21 * 28)) / 28;
        int jong = unicodeValue % 28;

        // 초성 계산
        char choSung = (char) (0x1100 + cho);
        // 중성 계산
        char jungSung = (char) (0x1161 + jung);
        // 종성 계산 (종성이 없는 경우를 처리)
        char jongSung = jong != 0 ? (char) (0x11A7 + jong) : '\0';

        // 결과 출력
        System.out.println("초성: " + choSung);
        System.out.println("중성: " + jungSung);
        System.out.println("종성: " + (jongSung != '\0' ? jongSung : "없음"));
    }*/

    public static Map<String, String> disassemble(char hangulChar){
        if(hangulChar < '가' || hangulChar > '힣'){
            return null;
        }

        int unicodeValue = hangulChar - '가';

        int cho = unicodeValue / (21 * 28);
        int jung = (unicodeValue % (21 * 28)) / 28;
        int jong = unicodeValue % 28;

        Map<String, String> result = new HashMap<>();
        result.put("first", String.valueOf((char) (0x1100 + cho)));
        result.put("middle", String.valueOf((char) (0x1161 + jung)));
        result.put("last", String.valueOf(jong != 0 ? (char) (0x11A7 + jong) : '\0'));

        return result;
    }

}
