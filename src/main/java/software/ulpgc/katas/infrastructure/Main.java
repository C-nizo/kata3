package software.ulpgc.katas.infrastructure;

import software.ulpgc.katas.architecture.ports.UserLoader;
import software.ulpgc.katas.architecture.ports.UserWriter;
import software.ulpgc.katas.architecture.operations.ProcessUsersOperation;
import software.ulpgc.katas.infrastructure.adapters.jsonfile.JsonUserWriter;
import software.ulpgc.katas.infrastructure.adapters.randomuser.RandomUserLoader;

import java.nio.file.Path;

public class Main {
    static void main() {
        String baseUrl = "https://randomuser.me/api";

        UserLoader loader = new RandomUserLoader(baseUrl, 20);
        UserWriter writer = new JsonUserWriter(Path.of("users.json"));

        ProcessUsersOperation useCase = new ProcessUsersOperation(loader, writer);

        useCase.execute();
    }
}
