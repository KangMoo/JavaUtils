package kangmoo.hangul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static kangmoo.hangul.CombineHangulCharacter.*;
import static kangmoo.hangul.Disassemble.disassembleHangulToGroups;
import static kangmoo.hangul.Index.excludeLastElement;
import static kangmoo.hangul.Index.joinString;
import static kangmoo.hangul.RemoveLastHangulCharacter.removeLastHangulCharacter;
import static kangmoo.hangul.Utils.*;

public class Hangul {
    private static final Logger log = LoggerFactory.getLogger(Hangul.class);

    public static boolean isHangulCharacter(String character) {
        return character.matches("^[가-힣]$");
    }

    public static boolean isHangulAlphabet(String character) {
        return character.matches("^[ㄱ-ㅣ]$");
    }

    public static String binaryAssembleHangulAlphabets(String source, String nextCharacter) {
        if (canBeJungsung(source + nextCharacter)) {
            return combineVowels(source, nextCharacter);
        }

        boolean isConsonantSource = !canBeJungsung(source);
        if (isConsonantSource && canBeJungsung(nextCharacter)) {
            return combineHangulCharacter(source, nextCharacter);
        }

        return joinString(source, nextCharacter);
    }

    public static String linkHangulCharacters(String source, String nextCharacter) {
        String sourceJamo = disassembleHangulToGroups(source).get(0);
        String lastJamo = excludeLastElement(sourceJamo).second();

        return joinString(removeLastHangulCharacter(source), combineHangulCharacter(lastJamo, nextCharacter));
    }

    public static String binaryAssembleHangulCharacters(String source, String nextCharacter) {
        if (!(isHangulCharacter(source) || isHangulAlphabet(source))) {
            throw new IllegalArgumentException("Invalid source character: " + source + ". Source must be one character.");
        }

        if (!isHangulAlphabet(nextCharacter)) {
            throw new IllegalArgumentException("Invalid next character: " + nextCharacter + ". Next character must be one of the chosung, jungsung, or jongsung.");
        }


        String sourceJamos = disassembleHangulToGroups(source).get(0);

        boolean isSingleCharacter = sourceJamos.length() == 1;
        if (isSingleCharacter) {
            String sourceCharacter = sourceJamos.substring(0, 1);
            return binaryAssembleHangulAlphabets(sourceCharacter, nextCharacter);
        }

        Pair<String, String> pair = excludeLastElement(sourceJamos);
        String restJamos = pair.first();
        String lastJamo = pair.second();

        boolean needLinking = canBeChosung(lastJamo) && canBeJungsung(nextCharacter);

        if (needLinking) {
            return linkHangulCharacters(source, nextCharacter);
        }


        Function<String, Function<String, Function<String, String>>> fixConsonant = CombineHangulCharacter::curriedCombineHangulCharacter;
        Function<String, Function<String, String>> combineJungsung = curriedCombineHangulCharacter(restJamos.substring(0, 1));

        if (canBeJungsung(lastJamo + nextCharacter)) {
            return combineJungsung.apply(lastJamo + nextCharacter).apply("");
        }

        if (canBeJungsung(lastJamo) && canBeJongsung(nextCharacter)) {
            return combineJungsung.apply(lastJamo).apply(nextCharacter);
        }

        Function<String, Function<String, String>> fixVowel = combineJungsung;
        Function<String, String> combineJongsung = fixVowel.apply(restJamos.length() > 1 ? restJamos.substring(1, 2) : "");

        String lastConsonant = lastJamo;

        if (Utils.hasSingleBatchim(source) && canBeJongsung(lastConsonant + nextCharacter)) {
            return combineJongsung.apply(lastConsonant + nextCharacter);
        }

        return joinString(source, nextCharacter);
    }

    public static String binaryAssembleHangul(String source, String nextCharacter) {
        Pair<String, String> pair = excludeLastElement(source);
        String rest = pair.first();
        String lastCharacter = pair.second();
        boolean needJoinString = lastCharacter.isBlank() || nextCharacter.isBlank();

        return joinString(rest.split("")).concat(
                needJoinString
                        ? joinString(lastCharacter, nextCharacter)
                        : binaryAssembleHangulCharacters(lastCharacter, nextCharacter));
    }
}
