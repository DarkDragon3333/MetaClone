package ru.metaclone.auth.service;

import org.springframework.stereotype.Service;
import ru.metaclone.auth.exception.UserNotFountException;
import ru.metaclone.auth.mapper.RegisteredUserEntityMapper;
import ru.metaclone.auth.data.requests.UserCredentials;
import ru.metaclone.auth.data.entity.CredentialsEntity;
import ru.metaclone.auth.repository.AuthRepository;

@Service
public class CredentialsService {
    private final AuthRepository authRepository;
    private final RegisteredUserEntityMapper registeredUserEntityMapper;

    private static final String USER_NOT_FOUND_MESSAGE = "User with this id not found";

    public CredentialsService(AuthRepository authRepository,
                              RegisteredUserEntityMapper registeredUserEntityMapper) {
        this.authRepository = authRepository;
        this.registeredUserEntityMapper = registeredUserEntityMapper;
    }

    public Long getUserIdByLogin(String login) {
        var user = authRepository.findByLogin(login);
        if (user != null) return user.getUserId();
        return null;
    }

    public CredentialsEntity getUserById(Long id) throws UserNotFountException {
        return authRepository.findById(id).orElseThrow(() -> new UserNotFountException(USER_NOT_FOUND_MESSAGE));
    }

    public boolean isUserExistWithLogin(String login) {
        return authRepository.findByLogin(login) != null;
    }

    public Long saveUserCredential(UserCredentials userCredentials) {
        var credentialsEntity = registeredUserEntityMapper.mapUserCredentials(userCredentials);
        authRepository.save(credentialsEntity);
        return credentialsEntity.getUserId();
    }
}
