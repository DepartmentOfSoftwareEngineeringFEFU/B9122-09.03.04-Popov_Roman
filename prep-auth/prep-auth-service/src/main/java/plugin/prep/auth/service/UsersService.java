package plugin.prep.auth.service;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.prep.auth.*;
import plugin.prep.auth.dto.*;
import plugin.prep.auth.enums.*;
import plugin.prep.auth.exceptions.*;
import plugin.prep.auth.mapper.*;
import plugin.prep.auth.repository.*;
import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final JwtAuthHolder authHolder;

    private final UsersRepository usersRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDto me() {
        var auth = authHolder.get();
        if (!(auth instanceof JwtAuth jwtAuth) || !jwtAuth.isAuthenticated()) {
            throw new AuthorizationException();
        }

        var user = usersRepository.findById(jwtAuth.getUserId())
            .orElseThrow(AuthorizationException::new);

        String grade;

        if (user.getGrade() == null) {
            grade = null;
        }else{
            grade = user.getGrade().name();
        }

        return UserDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .login(user.getLogin())
            .grade(grade)
            .learningTrackId(user.getLearningTrackId())
            .role(user.getRole().getCode())
            .build();
    }

    public UserDto changeGrade(UserChangeGradeDto request) {
        var user = usersRepository
            .findById(request.getUserId()).orElseThrow(() -> Exceptions.forbidden(
                "Доступ запрещен. Пользователь не зарегистрирован"
            ));

        var gradeEnum = GradeEnum.fromValue(request.getGrade());

        user.setGrade(gradeEnum);
        var userSaved = usersRepository.save(user);
        return userMapper.toDto(userSaved);
    }

}
