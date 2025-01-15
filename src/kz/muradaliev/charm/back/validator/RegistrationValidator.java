package kz.muradaliev.charm.back.validator;

import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.RegistrationDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import static kz.muradaliev.charm.back.utils.StringUtils.VALID_EMAIL_ADDRESS_REGEX;
import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationValidator implements Validator<RegistrationDto> {

    private final ProfileDao dao = ProfileDao.getInstance();

    private static final RegistrationValidator INSTANCE = new RegistrationValidator();

    public static RegistrationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(RegistrationDto dto) {
        ValidationResult result = new ValidationResult();
        if (isBlank(dto.getEmail()) || !VALID_EMAIL_ADDRESS_REGEX.matcher(dto.getEmail()).matches()) {
            result.add("error.email.invalid");
        } else if (dao.getAllEmails().contains(dto.getEmail())) {
            result.add("error.email.exist");
        }
        if (isBlank(dto.getPassword())) {
            result.add("error.password.invalid");
        }
        return result;
    }
}