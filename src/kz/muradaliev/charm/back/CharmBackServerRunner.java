package kz.muradaliev.charm.back;

import kz.muradaliev.charm.back.controller.LikeController;
import kz.muradaliev.charm.back.controller.ProfileController;
import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.service.ProfileService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static kz.muradaliev.charm.back.model.Commands.*;

public class CharmBackServerRunner {
    public static void main(String[] args) throws IOException {
        ProfileController profileController = new ProfileController(new ProfileService(new ProfileDao()));
        LikeController likeController = new LikeController();
        CharmHttpServer server = new CharmHttpServer(8081, 5, profileController, likeController);
        server.start();

    }
}
