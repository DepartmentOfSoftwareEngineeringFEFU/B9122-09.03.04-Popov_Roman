package plugin.prep.assessment.feature.tests.enums;

import plugin.prep.errors.*;

public enum QuestionDifficultyEnum {
    JUNIOR,
    MIDDLE,
    SENIOR;

    public static QuestionDifficultyEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return QuestionDifficultyEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {

            //todo: refactor message and http code
            throw Exceptions.badRequest("несуществующая сложность");
        }
    }
}
