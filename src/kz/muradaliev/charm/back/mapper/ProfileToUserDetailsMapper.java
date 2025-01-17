package kz.muradaliev.charm.back.mapper;

import kz.muradaliev.charm.back.dto.UserDetails;
import kz.muradaliev.charm.back.model.Profile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileToUserDetailsMapper implements Mapper<Profile, UserDetails> {

    private static final ProfileToUserDetailsMapper INSTANCE = new ProfileToUserDetailsMapper();

    public static ProfileToUserDetailsMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public UserDetails map(Profile profile) {
        return map(profile, new UserDetails());
    }

    @Override
    public UserDetails map(Profile profile, UserDetails dto) {
        dto.setId(profile.getId());
        dto.setEmail(profile.getEmail());
        dto.setRole(profile.getRole());
        return dto;
    }
}