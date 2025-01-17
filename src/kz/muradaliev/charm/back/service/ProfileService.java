package kz.muradaliev.charm.back.service;

import jakarta.servlet.http.Part;
import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.*;
import kz.muradaliev.charm.back.mapper.*;
import kz.muradaliev.charm.back.model.Profile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static kz.muradaliev.charm.back.utils.UrlUtils.getProfilePhotoPath;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileService {

    private static final ProfileService INSTANCE = new ProfileService();

    private final ProfileDao dao = ProfileDao.getInstance();

    private final ContentService contentService = ContentService.getInstance();

    private final ProfileToProfileGetDtoMapper profileToProfileGetDtoMapper = ProfileToProfileGetDtoMapper.getInstance();

    private final ProfileToUserDetailsMapper profileToUserDetailsMapper = ProfileToUserDetailsMapper.getInstance();

    private final ProfileUpdateDtoToProfileMapper profileUpdateDtoToProfileMapper = ProfileUpdateDtoToProfileMapper.getInstance();

    private final ProfileFullUpdateDtoToProfileMapper profileFullUpdateDtoToProfileMapper = ProfileFullUpdateDtoToProfileMapper.getInstance();

    private final CredentialsDtoToProfileMapper credentialsDtoToProfileMapper = CredentialsDtoToProfileMapper.getInstance();

    private final RegistrationDtoToProfileMapper registrationDtoToProfileMapper = RegistrationDtoToProfileMapper.getInstance();

    public static ProfileService getInstance() {
        return INSTANCE;
    }

    public Long save(RegistrationDto dto) {
        return dao.save(registrationDtoToProfileMapper.map(dto)).getId();
    }

    public Optional<ProfileGetDto> findById(Long id) {
        return dao.findById(id).map(profileToProfileGetDtoMapper::map);
    }

    public List<ProfileGetDto> findAll() {
        return dao.findAll().stream().map(profileToProfileGetDtoMapper::map).toList();
    }

    @SneakyThrows
    public void update(ProfileUpdateDto dto) throws IOException {
        Optional<Profile> optProfile = dao.findById(dto.getId());
        if (optProfile.isPresent()) {
            Part photo = dto.getPhoto();
            if (photo != null) {
                contentService.upload(
                        getProfilePhotoPath(dto.getId(), photo.getSubmittedFileName()),
                        photo.getInputStream()
                );
            }
            dao.update(profileUpdateDtoToProfileMapper.map(dto, optProfile.get()));
        }
    }

    @SneakyThrows
    public void update(ProfileFullUpdateDto dto) throws IOException {
        Optional<Profile> optProfile = dao.findById(dto.getId());
        if (optProfile.isPresent()) {
            Part photo = dto.getPhoto();
            if (photo != null) {
                contentService.upload(
                        getProfilePhotoPath(dto.getId(), photo.getSubmittedFileName()),
                        photo.getInputStream()
                );
            }
            dao.update(profileFullUpdateDtoToProfileMapper.map(dto, optProfile.get()));
        }
    }

    public void update(CredentialsDto dto) {
        dao.findById(dto.getId()).ifPresent(profile -> dao.update(credentialsDtoToProfileMapper.map(dto, profile)));
    }

    public boolean delete(Long id) {
        return dao.delete(id);
    }

    public Optional<UserDetails> getUserDetails(String email) {
        return dao.findByEmail(email).map(profileToUserDetailsMapper::map);
    }
}