package software.ulpgc.katas.infrastructure.adapters.randomuser;

import software.ulpgc.katas.architecture.model.User;
import software.ulpgc.katas.architecture.ports.UserLoader;

import java.util.List;

public class RandomUserLoader implements UserLoader {
    private final UserFetcher fetcher;
    private final UserTransformer transformer = new UserTransformer();

    public RandomUserLoader(String baseUrl, int count) {
        this.fetcher = new UserFetcher(baseUrl, count);
    }

    @Override
    public List<User> loadUsers() {
        return transformer.transform(fetcher.fetchUsers());
    }
}
