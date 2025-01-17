package kz.muradaliev.charm.back.validator;

import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.CredentialsDto;
import kz.muradaliev.charm.back.model.Profile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static kz.muradaliev.charm.back.utils.StringUtils.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CredentialsValidator implements Validator<CredentialsDto> {

    private final ProfileDao dao = ProfileDao.getInstance();

    private static final CredentialsValidator INSTANCE = new CredentialsValidator();

    public static CredentialsValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(CredentialsDto dto) {
        ValidationResult result = new ValidationResult();
        Profile profile = dao.findById(dto.getId()).orElseThrow();
        if (!dto.getCurrentPassword().equals(profile.getPassword())) {
            result.add("error.password.invalid");
        }
        if (dto.getEmail() != null) {
            if (!isValidEmail(dto.getEmail())) {
                result.add("error.email.invalid");
            }
            if (dao.getAllEmails().contains(dto.getEmail())) {
                result.add("error.email.exist");
            }
        }
        if (!isValidPassword(dto.getNewPassword()) && !dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            result.add("error.password.invalid");
        }
        return result;
    }
}