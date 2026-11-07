package software.ulpgc.katas.infrastructure.randomuser;

public class UserDto {
    Name name;
    Dob dob;

    public record Name(String first, String last) {
    }

    public record Dob(String date) {
    }
}
