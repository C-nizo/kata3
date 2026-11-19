package software.ulpgc.katas.infrastructure.adapters.randomuser;

import software.ulpgc.katas.architecture.model.User;

import java.time.Instant;

public class UserMapper {
    public User map(UserDto dto) {
        String name;
        Instant birthday;
        String email;

        name = dto.name().first() + " " + dto.name().last();
        birthday = Instant.parse(dto.dob().date());

        email = dto.email();

        return new User(name, birthday, email);
    }
}
