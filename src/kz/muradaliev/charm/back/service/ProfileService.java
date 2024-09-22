package kz.muradaliev.charm.back.service;

import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.model.Profile;

import java.util.Optional;

public class ProfileService {
    private final ProfileDao dao;

    public ProfileService(ProfileDao dao) {
        this.dao = dao;
    }

    public Profile save (Profile profile){
        return dao.save(profile);
    }

    public Optional<Profile> findById(Long id) {
        if (id==null) return Optional.empty();
        return dao.findById(id);
    }

    //TODO delete, update, findAll
}
