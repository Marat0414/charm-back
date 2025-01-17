package kz.muradaliev.charm.back.validator;

import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.LoginDto;
import kz.muradaliev.charm.back.model.Profile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static kz.muradaliev.charm.back.utils.StringUtils.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginValidator implements Validator<LoginDto> {

    private final ProfileDao dao = ProfileDao.getInstance();

    private static final LoginValidator INSTANCE = new LoginValidator();

    public static LoginValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(LoginDto dto) {
        ValidationResult result = new ValidationResult();
        if (!isValidEmail(dto.getEmail())) {
            result.add("error.email.invalid");
        }
        Optional<Profile> profile = dao.findByEmail(dto.getEmail());
        if (profile.isEmpty()) {
            result.add("error.email.missing");
        } else if (isBlank(dto.getPassword()) || !dto.getPassword().equals(profile.get().getPassword())) {
            result.add("error.password.invalid");
        }
        return result;
    }
}
