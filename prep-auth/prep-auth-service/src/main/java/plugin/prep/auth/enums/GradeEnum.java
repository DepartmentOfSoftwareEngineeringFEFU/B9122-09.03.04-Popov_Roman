package plugin.prep.auth.enums;

import lombok.*;

import plugin.prep.errors.*;

@Getter
@RequiredArgsConstructor
public enum GradeEnum {
    JUNIOR,
    MIDDLE,
    SENIOR;

    public static GradeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return GradeEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {

            //todo: refactor message and http code
            throw Exceptions.badRequest("несуществующий уровень");
        }
    }
}
