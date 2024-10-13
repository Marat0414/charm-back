package kz.muradaliev.charm.back;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class CharmBackClientRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        try (HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://yandex.ru"))
//                    .setHeader("My-Token", "get_off_my_way")
                    .GET()
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            HttpClient.Version version = response.version();
            int statusCode = response.statusCode();
            String headers = response.headers().map().entrySet().stream().map(Objects::toString).collect(Collectors.joining(","));
            String body = new String(response.body());
            System.out.println(version + " " + statusCode + "\n" + headers + "\n\n" + body);
        }
    }
}