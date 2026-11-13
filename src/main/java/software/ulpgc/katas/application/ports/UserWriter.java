package software.ulpgc.katas.application.ports;

import software.ulpgc.katas.application.domain.model.User;

import java.util.List;

public interface UserWriter {
    void write(List<User> users);
}
