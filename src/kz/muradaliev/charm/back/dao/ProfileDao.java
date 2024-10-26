package kz.muradaliev.charm.back.dao;

import kz.muradaliev.charm.back.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileDao {
    private final ConcurrentHashMap<Long, Profile> storage;
    private final AtomicLong idStorage;

    public ProfileDao() {
        this.storage = new ConcurrentHashMap<>();
        Profile profile1 = new Profile();
        profile1.setId(1L);
        profile1.setEmail("ivanov@mail.ru");
        profile1.setName("Ivan");
        profile1.setSurname("Ivanov");
        profile1.setAbout("Man");
        this.storage.put(1L, profile1);

        Profile profile2 = new Profile();
        profile2.setId(2L);
        profile2.setEmail("smirnov@mail.ru");
        profile2.setName("Semyon");
        profile2.setSurname("Smirnov");
        profile2.setAbout("Man");
        this.storage.put(2L, profile2);
        this.idStorage = new AtomicLong(3L);


    }

    public Profile save(Profile profile) {
        long id = idStorage.getAndIncrement();
        profile.setId(id);
        storage.put(profile.getId(), profile);
        System.out.println(storage.values());
        return profile;
    }

    public Optional<Profile> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Profile> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void update(Profile profile) {
        Long id = profile.getId();
        if (id == null) return;
        storage.put(id, profile);
    }

    public boolean delete(Long id) {
        if (id == null) return false;
        return storage.remove(id) != null;
    }


}
