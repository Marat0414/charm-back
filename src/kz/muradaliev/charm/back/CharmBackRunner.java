package kz.muradaliev.charm.back;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CharmBackRunner {
    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 8081);
             DataOutputStream rqStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream rsStream = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (scanner.hasNextLine()) {
                String request = scanner.nextLine();
                rqStream.writeUTF(request);
                String response = rsStream.readUTF();
                System.out.println(response);
            }
        }
    }
}