package software.ulpgc.katas.architecture.ports;

import software.ulpgc.katas.architecture.model.User;
import java.util.List;

public interface UserWriter {
    void write(List<User> users);
}

