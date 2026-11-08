package software.ulpgc.katas.application.usecases;

import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.application.ports.UserOutput;
import software.ulpgc.katas.application.ports.UserStore;

import java.util.List;

public class ProcessUsersUseCase {
    private final UserStore store;
    private final UserOutput output;

    public ProcessUsersUseCase(UserStore store, UserOutput output) {
        this.store = store;
        this.output = output;
    }

    public void execute() {
        List<User> users = store.load();
        output.write(users);
    }
}
