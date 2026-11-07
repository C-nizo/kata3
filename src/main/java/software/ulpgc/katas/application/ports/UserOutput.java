package software.ulpgc.katas.application.ports;

import software.ulpgc.katas.application.domain.model.User;

import java.util.List;

public interface UserOutput {
    void write(List<User> users);
}
