package software.ulpgc.katas.application.domain.model;

import java.time.Instant;

public record User(String name, Instant birthday) {
}
