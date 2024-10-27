package kz.muradaliev.charm.back.service;

public class LikeService {
    private static final LikeService INSTANCE = new LikeService();

    private LikeService() {
    }

    public static LikeService getInstance() {
        return INSTANCE;
    }

    public long getLikesById(long id) {
        return 10 + id;
    }
}
