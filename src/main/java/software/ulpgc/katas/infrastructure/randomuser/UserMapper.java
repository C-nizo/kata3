package software.ulpgc.katas.infrastructure.randomuser;

import software.ulpgc.katas.application.domain.model.User;

import java.time.Instant;

public class UserMapper {
    public User map(UserDto dto) {
        String name;
        Instant birthday;

        name = dto.name.first() + " " + dto.name.last();
        birthday = Instant.parse(dto.dob.date());

        return new User(name, birthday);
    }
}
