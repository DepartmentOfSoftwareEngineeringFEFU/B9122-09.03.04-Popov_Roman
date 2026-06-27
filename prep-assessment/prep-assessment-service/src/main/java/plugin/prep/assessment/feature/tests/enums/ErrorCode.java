package plugin.prep.assessment.feature.tests.enums;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TEST_ALREADY_EXISTS("Тест уже существует: title=%s"),
    TEST_NOT_FOUND("Тест не найден: id=%s"),
    QUESTION_NOT_FOUND("Вопрос не найден: id=%s"),
    ANSWER_ALREADY_EXISTS("Ответ уже существует: questionId=%s, text=%s"),
    ANSWER_NOT_FOUND("Ответ не найден: answerId=%s"),
    USER_TEST_SESSION_NOT_FOUND("Сессия пользователя не найдена: id=%s"),
    USER_TEST_SESSION_ALREADY_COMPLETE("Тест уже завершен: id=%s"),
    USER_TEST_SESSION_NOT_COMPLETE("Нельзя начать новый тест не закончив предыдущий: userTestSessionId=%s");

    private final String message;

    public String format(Object... args) {
        return message.formatted(args);
    }
}
