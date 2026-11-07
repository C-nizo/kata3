package software.ulpgc.katas.application.usecases;

import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.application.ports.UserStore;
import software.ulpgc.katas.application.ports.UserOutput;

import java.util.List;

public class GenerateUsersJsonUseCase {
    private final UserStore store;
    private final UserOutput output;

    public GenerateUsersJsonUseCase(UserStore store, UserOutput output) {
        this.store = store;
        this.output = output;
    }

    public void execute() {
        List<User> users = store.load();
        output.write(users);
    }
}