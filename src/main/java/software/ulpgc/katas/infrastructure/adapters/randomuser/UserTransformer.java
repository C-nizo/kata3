package software.ulpgc.katas.infrastructure.adapters.randomuser;

import software.ulpgc.katas.architecture.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserTransformer {
    private final UserValidator validator = new UserValidator();
    private final UserMapper mapper = new UserMapper();

    public List<User> transform(List<UserDto> userDtos) {
        List<User> users = new ArrayList<>();

        for (UserDto dto : userDtos) {
            if (validator.isValid(dto)) {
                users.add(mapper.map(dto));
            }
        }

        return users;
    }
}
