import com.github.kangmoo.utils.ValidationUtil;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author kangmoo Heo
 */
public class ValidationTest {
    @Min(0)
    int age = -1;

    @Test
    void test(){
        assertThrows(ValidationException.class, () -> {
            ValidationUtil.validCheck(this);
        });
    }
}
