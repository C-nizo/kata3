package software.ulpgc.katas.application.usecases;

import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.application.ports.UserWriter;
import software.ulpgc.katas.application.ports.UserLoader;

import java.util.List;

public class ProcessUsersUseCase {
    private final UserLoader loader;
    private final UserWriter writer;

    public ProcessUsersUseCase(UserLoader loader, UserWriter writer) {
        this.loader = loader;
        this.writer = writer;
    }

    public void execute() {
        List<User> users = loader.load();
        writer.write(users);
    }
}
