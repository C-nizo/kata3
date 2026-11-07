package software.ulpgc.katas.infrastructure.randomuser;

public record UserDto(Name name, Dob dob) {
    record Name(String first, String last) {
    }

    record Dob(String date) {
    }
}
