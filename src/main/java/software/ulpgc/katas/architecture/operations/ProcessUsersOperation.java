package software.ulpgc.katas.architecture.operations;

import software.ulpgc.katas.architecture.model.User;
import software.ulpgc.katas.architecture.ports.UserLoader;
import software.ulpgc.katas.architecture.ports.UserWriter;

import java.util.List;

public class ProcessUsersOperation {
    private final UserLoader loader;
    private final UserWriter writer;

    public ProcessUsersOperation(UserLoader loader, UserWriter writer) {
        this.loader = loader;
        this.writer = writer;
    }

    public void execute() {
        List<User> users = loader.loadUsers();
        writer.write(users);
    }
}
