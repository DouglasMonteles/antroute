package br.com.doug.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void get(String path) {
        try {
             URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection conn = getHttpURLConnection(url, "GET");

            // Verificando a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Código de resposta: " + responseCode);

            assert responseCode == 200;

            conn.disconnect();
        } catch (Exception e) {
            LOG.error("Error during http get request. Error: {}", e.getMessage());
        }
    }

    public static int post(String path, Object data) {
        int responseCode = -1;

        try {
            URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection conn = getHttpURLConnection(url, "POST");
            conn.connect();

            String jsonInput = mapper.writeValueAsString(data);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            responseCode = conn.getResponseCode();

            conn.disconnect();
        } catch (IOException e) {
            LOG.error("Error during http post request. Error: {}", e.getMessage());
        } catch (URISyntaxException e) {
            LOG.error("Error in http post syntax request. Error: {}", e.getMessage());
        }

        return responseCode;
    }

    private static HttpURLConnection getHttpURLConnection(URL url, String method) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        return conn;
    }

}
