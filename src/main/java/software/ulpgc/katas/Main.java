package software.ulpgc.katas;

import software.ulpgc.katas.application.ports.UserStore;
import software.ulpgc.katas.application.ports.UserOutput;
import software.ulpgc.katas.application.usecases.GenerateUsersJsonUseCase;
import software.ulpgc.katas.infrastructure.adapters.RandomUserStore;
import software.ulpgc.katas.infrastructure.adapters.JsonUserOutput;

import java.nio.file.Path;

public class Main {
    static void main() {
        UserStore store = new RandomUserStore(20);
        UserOutput output = new JsonUserOutput(Path.of("users.json"));

        GenerateUsersJsonUseCase useCase =
                new GenerateUsersJsonUseCase(store, output);

        useCase.execute();
    }
}