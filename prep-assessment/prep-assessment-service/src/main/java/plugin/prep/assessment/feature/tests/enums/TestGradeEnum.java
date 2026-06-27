package plugin.prep.assessment.feature.tests.enums;

import lombok.*;
import plugin.prep.errors.*;

@Getter
@RequiredArgsConstructor
public enum TestGradeEnum {
    JUNIOR,
    MIDDLE,
    SENIOR;

    public static TestGradeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return TestGradeEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw Exceptions.badRequest("несуществующая сложность");
        }
    }
}
