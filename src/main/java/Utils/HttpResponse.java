package Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final int responseCode;
    private final String responseText;

    public HttpResponse(HttpURLConnection connection) throws IOException {
        this.responseCode = connection.getResponseCode();
        this.responseText = this.buildResponseText(this.isSuccess() ? connection.getInputStream() : connection.getErrorStream());
    }

    public JSONObject getResponseJson() {
        try {
            return new JSONObject(this.responseText);
        } catch (Exception exception) {
            return null;
        }
    }

    public String getResponseText() {
        return this.responseText;
    }

    public boolean isSuccess() {
        return this.responseCode == 200;
    }

    private String buildResponseText(InputStream inputStream) throws IOException {
        StringBuilder responseText = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseText.append(inputLine);
            }
        }
        return responseText.toString();
    }
}
