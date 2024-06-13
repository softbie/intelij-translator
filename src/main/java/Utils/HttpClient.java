package Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClient {
    public static HttpResponse get(String url) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestMethod("GET");

        return new HttpResponse(connection);
    }

    public static HttpResponse get(String url, Map<String, String> params) throws IOException {
        url = url + getQueryString(params);
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestMethod("GET");

        return new HttpResponse(connection);
    }

    private static String getQueryString(Map<String, String> params) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryString.length() > 1) {
                queryString.append("&");
            }
            queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return queryString.toString();
    }
}
