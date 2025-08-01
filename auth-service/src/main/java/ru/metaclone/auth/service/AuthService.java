package ru.metaclone.auth.service;

import org.springframework.stereotype.Service;
import ru.metaclone.auth.exception.InvalidTokenException;
import ru.metaclone.auth.exception.UserAlreadyExistException;
import ru.metaclone.auth.exception.UserNotFountException;
import ru.metaclone.auth.mapper.UserDetailsEventMapper;
import ru.metaclone.auth.data.response.LogoutResponse;
import ru.metaclone.auth.data.requests.UserCredentials;
import ru.metaclone.auth.data.requests.UserDetails;
import ru.metaclone.auth.data.response.TokensResponse;

import java.util.List;

@Service
public class AuthService {
    private final TokensService tokensService;
    private final CredentialsService credentialsService;
    private final UserDetailsEventMapper userDetailsEventMapper;
    private final UsersDetailsProducer usersDetailsProducer;

    private static final String USER_ALREADY_EXIST = "User with this login already exist";
    private static final String USER_NOT_FOUND = "User with this login not found";
    private static final String LOGOUT_SUCCESSFUL = "Logout successful";

    public AuthService(
            TokensService tokensService,
            CredentialsService credentialsService,
            UsersDetailsProducer usersDetailsProducer,
            UserDetailsEventMapper userDetailsEventMapper
    ) {
        this.credentialsService = credentialsService;
        this.tokensService = tokensService;
        this.usersDetailsProducer = usersDetailsProducer;
        this.userDetailsEventMapper = userDetailsEventMapper;
    }

    public TokensResponse loginUser(UserCredentials credentials, List<String> defaultAuthorities)
            throws UserNotFountException {
        var userId = credentialsService.getUserIdByLogin(credentials.login());
        if (userId == null) {
            throw new UserNotFountException(USER_NOT_FOUND);
        }
        return tokensService.generateAndSaveTokens(userId, defaultAuthorities);
    }

    public TokensResponse registerUser(
            UserCredentials userCredentials,
            UserDetails userDetails,
            List<String> defaultAuthorities
    ) throws UserAlreadyExistException {
        if (credentialsService.isUserExistWithLogin(userCredentials.login())) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        var userId = credentialsService.saveUserCredential(userCredentials);
        usersDetailsProducer.sendUserInfo(
                userDetailsEventMapper.mapUserDetailsEvent(userId, userCredentials.login(), userDetails));
        var result = tokensService.generateAndSaveTokens(userId, defaultAuthorities);
        return result;
    }

    public TokensResponse refreshAccessToken(String refreshToken) throws InvalidTokenException {
        return tokensService.refreshAccessToken(refreshToken);
    }

    public LogoutResponse logout(String refreshToken) {
        tokensService.logout(refreshToken);
        return new LogoutResponse(LOGOUT_SUCCESSFUL);
    }
}
