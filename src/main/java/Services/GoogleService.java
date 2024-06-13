package Services;

import Interfaces.ITranslatable;
import Utils.HttpClient;
import Utils.HttpResponse;
import org.json.JSONObject;

import java.util.Map;

public class GoogleService implements ITranslatable {
    private final String apiUrl = "https://translate.googleapis.com/translate_a/single";

    @Override
    public String translate(String from, String to, String text) {
        String result = null;
        try {
            HttpResponse response = HttpClient.get(apiUrl, Map.of(
                    "client", "gtx",
                    "sl", from,
                    "tl", to,
                    "dt", "t",
                    "dj", "1",
                    "q", text
            ));
            JSONObject json = response.getResponseJson();
            if (json != null && !json.getJSONArray("sentences").isEmpty()) {
                JSONObject sentenceObject = (JSONObject) json.getJSONArray("sentences").get(0);
                result = sentenceObject.getString("trans");
            }
        } catch (Exception ignored) {
        }

        return result;
    }
}
