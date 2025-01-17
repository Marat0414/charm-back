package kz.muradaliev.charm.back.mapper;

import kz.muradaliev.charm.back.dto.CredentialsDto;
import kz.muradaliev.charm.back.model.Profile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CredentialsDtoToProfileMapper implements Mapper<CredentialsDto, Profile> {

    private static final CredentialsDtoToProfileMapper INSTANCE = new CredentialsDtoToProfileMapper();

    public static CredentialsDtoToProfileMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Profile map(CredentialsDto dto) {
        return map(dto, new Profile());
    }

    @Override
    public Profile map(CredentialsDto dto, Profile profile) {
        profile.setId(dto.getId());
        if (!isBlank(dto.getEmail())) {
            profile.setEmail(dto.getEmail());
        }
        if (!isBlank(dto.getNewPassword())) {
            profile.setPassword(dto.getNewPassword());
        }
        return profile;
    }
}