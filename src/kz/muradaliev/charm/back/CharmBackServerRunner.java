package kz.muradaliev.charm.back;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CharmBackServerRunner {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8081);
             Socket socket = serverSocket.accept();
             DataInputStream rqStream = new DataInputStream(socket.getInputStream());
             DataOutputStream rsStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String request = rqStream.readUTF();

            while(!"stop".equals(request)){
                String response = "Hi from server";
                rsStream.writeUTF(response);
                request = rqStream.readUTF();
            }
        }
    }
}
