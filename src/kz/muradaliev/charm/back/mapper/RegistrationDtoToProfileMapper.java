package kz.muradaliev.charm.back.mapper;

import kz.muradaliev.charm.back.dto.RegistrationDto;
import kz.muradaliev.charm.back.model.Profile;
import kz.muradaliev.charm.back.model.Role;
import kz.muradaliev.charm.back.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static kz.muradaliev.charm.back.model.Role.USER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationDtoToProfileMapper implements Mapper<RegistrationDto, Profile>{

    private static final RegistrationDtoToProfileMapper INSTANCE = new RegistrationDtoToProfileMapper();

    public static RegistrationDtoToProfileMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Profile map(RegistrationDto dto) {
        return map(dto, new Profile());
    }

    @Override
    public Profile map(RegistrationDto dto, Profile profile) {
        profile.setEmail(dto.getEmail());
        profile.setPassword(dto.getPassword());
        profile.setStatus(Status.INACTIVE);
        profile.setRole(Role.USER);
        return profile;
    }


}
