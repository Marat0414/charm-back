package kz.muradaliev.charm.back.validator;

import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.ProfileUpdateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static kz.muradaliev.charm.back.utils.DateTimeUtils.getAge;
import static kz.muradaliev.charm.back.utils.DateTimeUtils.isValidAge;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileUpdateValidator implements Validator<ProfileUpdateDto> {

    private static final ProfileUpdateValidator INSTANCE = new ProfileUpdateValidator();

    public static ProfileUpdateValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult validate(ProfileUpdateDto dto) {
        ValidationResult result = new ValidationResult();
        if (dto.getBirthDate() != null && !isValidAge(dto.getBirthDate())) {
            result.add("error.age.invalid");
        }
        return result;
    }
}