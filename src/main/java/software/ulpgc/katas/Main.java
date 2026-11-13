package software.ulpgc.katas;

import software.ulpgc.katas.application.ports.UserWriter;
import software.ulpgc.katas.application.ports.UserLoader;
import software.ulpgc.katas.application.usecases.ProcessUsersUseCase;
import software.ulpgc.katas.infrastructure.adapters.JsonUserWriter;
import software.ulpgc.katas.infrastructure.adapters.RandomUserLoader;

import java.nio.file.Path;

public class Main {
    static void main() {
        UserLoader loader = new RandomUserLoader(20);
        UserWriter writer = new JsonUserWriter(Path.of("users.json"));

        ProcessUsersUseCase useCase = new ProcessUsersUseCase(loader, writer);

        useCase.execute();
    }
}
