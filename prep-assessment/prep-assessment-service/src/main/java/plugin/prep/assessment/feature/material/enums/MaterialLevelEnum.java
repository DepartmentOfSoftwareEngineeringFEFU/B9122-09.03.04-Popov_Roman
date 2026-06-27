package plugin.prep.assessment.feature.material.enums;

import lombok.*;

import plugin.prep.errors.*;

@Getter
public enum MaterialLevelEnum {
    JUNIOR,
    MIDDLE,
    SENIOR;

    public static MaterialLevelEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return MaterialLevelEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {

            //todo: refactor message and http code
            throw Exceptions.badRequest("несуществующая сложность");
        }
    }
}
