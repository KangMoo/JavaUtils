package kangmoo.hangul;

import java.util.Map;
import java.util.function.Function;

public class CombineHangulCharacter {
    public static String combineHangulCharacter(String firstCharacter, String middleCharacter) {
        return combineHangulCharacter(firstCharacter, middleCharacter, "");
    }

    public static String combineHangulCharacter(String firstCharacter, String middleCharacter, String lastCharacter) {
        if (firstCharacter == null) firstCharacter = "";
        if (middleCharacter == null) middleCharacter = "";
        if (lastCharacter == null) lastCharacter = "";
        if (!Utils.canBeChosung(firstCharacter) || !Utils.canBeJungsung(middleCharacter) || !Utils.canBeJongsung(lastCharacter)) {
            throw new IllegalArgumentException("Invalid hangul Characters: " + firstCharacter + ", " + middleCharacter + ", " + lastCharacter);
        }

        int numOfMiddleCharacters = Constants.HANGUL_CHARACTERS_BY_MIDDLE_INDEX.size();
        int numOfLastCharacters = Constants.HANGUL_CHARACTERS_BY_LAST_INDEX.size();

        int firstCharacterIndex = Constants.HANGUL_CHARACTERS_BY_FIRST_INDEX.indexOf(firstCharacter);
        int middleCharacterIndex = Constants.HANGUL_CHARACTERS_BY_MIDDLE_INDEX.indexOf(middleCharacter);
        int lastCharacterIndex = Constants.HANGUL_CHARACTERS_BY_LAST_INDEX.indexOf(lastCharacter);

        int firstIndexOfTargetConsonant = firstCharacterIndex * numOfMiddleCharacters * numOfLastCharacters;
        int firstIndexOfTargetVowel = middleCharacterIndex * numOfLastCharacters;

        int unicode = Constants.COMPLETE_HANGUL_START_CHARCODE + firstIndexOfTargetConsonant + firstIndexOfTargetVowel + lastCharacterIndex;

        return String.valueOf((char) unicode);
    }

    /**
     * @name curriedCombineHangulCharacter
     * @description
     * 인자로 초성, 중성, 종성을 받아 하나의 한글 문자를 반환하는 `combineHangulCharacter` 함수의 커링된 버전입니다.
     * @example
     * const combineMiddleHangulCharacter = curriedCombineHangulCharacter('ㄱ')
     * const combineLastHangulCharacter = combineMiddleHangulCharacter('ㅏ')
     * combineLastHangulCharacter('ㄱ') // '각'
     */
//    export const curriedCombineHangulCharacter =
//            (firstCharacter: string) =>
//            (middleCharacter: string) =>
//            (lastCharacter = '') =>
//    combineHangulCharacter(firstCharacter, middleCharacter, lastCharacter);

    public static Function<String, Function<String, String>> curriedCombineHangulCharacter(String firstCharacter) {
        return middleCharacter -> lastCharacter -> combineHangulCharacter(firstCharacter, middleCharacter, lastCharacter);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String combineVowels(String vowel1, String vowel2) {
        for (Map.Entry<String, String> entry : Constants.DISASSEMBLED_VOWELS_BY_VOWEL.entrySet()) {
            if (entry.getValue().equals(vowel1 + vowel2)) {
                return entry.getKey();
            }
        }
        return vowel1 + vowel2;
    }


}
