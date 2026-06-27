package plugin.prep.auth.service;

import java.time.*;
import java.util.*;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.prep.auth.*;
import plugin.prep.auth.client.learningtrack.*;
import plugin.prep.auth.dto.*;
import plugin.prep.auth.entity.*;
import plugin.prep.auth.exceptions.*;
import plugin.prep.auth.repository.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE_CODE = "ROLE_USER";

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final CredentialsRepository credentialsRepository;

    private final RefreshTokensRepository refreshRepository;

    private final JwtProperties jwtProps;

    private final JwtProvider jwtProvider;

    private final JwtVerifier jwtVerifier;

    private final LearningTracksClient learningTracksClient;

    @Transactional
    public AuthDto register(RegisterRequest request) {
        var login = request.getLogin().trim();
        var email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        var learningTrackId = request.getLearningTrackId();

        if (usersRepository.existsByLogin(login) || usersRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException();
        }

        if (!learningTrackExists(learningTrackId)) {
            throw new AuthAppException(
                "Направление подготовки не найдено",
                HttpStatus.BAD_REQUEST);
        }

        var role = rolesRepository.findByCode(DEFAULT_ROLE_CODE);
        if (role == null) {
            throw new AuthAppException(
                "Роль пользователя не настроена",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var user = User.builder()
            .login(login)
            .email(email)
            .learningTrackId(learningTrackId)
            .role(role)
            .build();
        user = usersRepository.save(user);

        var salt = BCrypt.gensalt();
        var hash = BCrypt.hashpw(request.getPassword(), salt);
        var credential = Credential.builder()
            .user(user)
            .salt(salt)
            .hash(hash)
            .build();
        credentialsRepository.save(credential);

        return issueTokens(user);
    }

    @Transactional
    public AuthDto login(LoginRequest request) {
        var user = usersRepository.findByLogin(request.getLogin());
        if (user == null) {
            throw new AuthenticationException();
        }

        var credential = credentialsRepository.findByUserId(user.getId());
        if (credential == null || !BCrypt.checkpw(request.getPassword(), credential.getHash())) {
            throw new AuthenticationException();
        }

        return issueTokens(user);
    }

    @Transactional
    public AuthDto refresh(TokenRequest request) {
        var tokenValue = request.getToken();
        var decoded = jwtVerifier.verify(tokenValue);

        var jti = UUID.fromString(decoded.getId());
        var old = refreshRepository.findByRefreshJti(jti);
        if (old == null) {
            throw new JwtTokenException();
        }

        var user = usersRepository.findById(old.getUserId())
            .orElseThrow(JwtTokenException::new);

        refreshRepository.deleteByRefreshJti(jti);
        return issueTokens(user);
    }

    @Transactional
    public void logout(TokenRequest request) {
        var token = request.getToken();
        var decoded = jwtVerifier.verify(token);
        var userId = decoded.getClaim("user_id").asLong();

        refreshRepository.deleteByUserId(userId);
    }

    private AuthDto issueTokens(User user) {
        var principal = buildPrincipal(user);

        var accessJti = UUID.randomUUID();
        var refreshJti = UUID.randomUUID();
        var access = jwtProvider.generateAccess(accessJti, principal);
        var refresh = jwtProvider.generateRefresh(refreshJti, principal);

        var now = ZonedDateTime.now();
        var expireAt = now.plusSeconds(jwtProps.getRefreshTokenExpiredOnSeconds());

        var token = RefreshToken.builder()
            .userId(user.getId())
            .token(refresh)
            .accessJti(accessJti)
            .refreshJti(refreshJti)
            .issuedAt(now)
            .expiredAt(expireAt)
            .build();

        refreshRepository.save(token);

        return new AuthDto(access, refresh);
    }

    private Principal buildPrincipal(User user) {
        return Principal.builder()
            .id(user.getId())
            .login(user.getLogin())
            .roles(List.of(user.getRole().getCode()))
            .build();
    }

    private boolean learningTrackExists(Long learningTrackId) {
        var tracks = learningTracksClient.getAll();
        return tracks.stream()
            .anyMatch(track -> learningTrackId.equals(track.getId()));
    }

}
