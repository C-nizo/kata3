package software.ulpgc.katas.application.ports;

import software.ulpgc.katas.application.domain.model.User;

import java.util.List;

public interface UserLoader {
    List<User> load();
}
