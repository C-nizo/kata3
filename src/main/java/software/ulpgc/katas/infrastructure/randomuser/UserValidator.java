package software.ulpgc.katas.infrastructure.randomuser;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class UserValidator {
    public boolean isValid(UserDto dto) {
        return dto != null
                && validName(dto.name())
                && validDate(dto.dob())
                && validEmail(dto.email());
    }

    private boolean validName(UserDto.Name name) {
        return name != null
                && hasText(name.first())
                && hasText(name.last());
    }

    private boolean validDate(UserDto.Dob dob) {
        return dob != null
                && hasText(dob.date())
                && validDateFormat(dob.date());
    }

    private boolean validDateFormat(String date) {
        try {
            Instant.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean validEmail(String email) {
        return hasText(email) &&
                email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private boolean hasText(String text) {
        return text != null && !text.isBlank();
    }
}
