package ru.metaclone.users.mappers;

import org.springframework.stereotype.Component;
import ru.metaclone.users.data.response.UserResponse;
import ru.metaclone.users.data.entity.UserEntity;
import ru.metaclone.users.data.events.UserCreatedEvent;

@Component
public class UserEntityMapper {
    public UserEntity mapEntityFrom(UserCreatedEvent userCreatedEvent) {
        return UserEntity.builder()
                .userId(userCreatedEvent.userId())
                .login(userCreatedEvent.login())
                .firstName(userCreatedEvent.firstName())
                .secondName(userCreatedEvent.lastName())
                .gender(userCreatedEvent.gender() != null ? userCreatedEvent.gender().name() : null)
                .birthday(userCreatedEvent.birthday())
                .build();
    }

    public UserResponse mapToResponse(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getUserId(),
                userEntity.getLogin(),
                userEntity.getFirstName(),
                userEntity.getSecondName(),
                userEntity.getAvatarUrl(),
                userEntity.getAbout(),
                userEntity.getBirthday(),
                userEntity.getGender(),
                userEntity.getFollowsCount(),
                userEntity.getFollowersCount()
        );
    }
}
