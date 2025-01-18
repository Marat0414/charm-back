package kz.muradaliev.charm.back.validator;

import kz.muradaliev.charm.back.dao.InMemoryProfileDao;
import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.RegistrationDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static kz.muradaliev.charm.back.utils.StringUtils.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationValidator implements Validator<RegistrationDto> {

    private final ProfileDao dao = InMemoryProfileDao.getInstance();

    private static final RegistrationValidator INSTANCE = new RegistrationValidator();

    public static RegistrationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(RegistrationDto dto) {
        ValidationResult result = new ValidationResult();
        if (!isValidEmail(dto.getEmail())) {
            result.add("error.email.invalid");
        } else if (dao.existByEmail(dto.getEmail())) {
            result.add("error.email.exist");
        }
        if (!isValidPassword(dto.getPassword()) || !dto.getPassword().equals(dto.getConfirm())) {
            result.add("error.password.invalid");
        }
        return result;
    }
}