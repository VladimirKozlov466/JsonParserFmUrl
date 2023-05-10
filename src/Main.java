import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JSONObject parsedFromUrl;

        {
            try {
                parsedFromUrl = JsonFmUrl.getJsonByUrl();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        JSONObject jsonCurrenciesOnly = parsedFromUrl.getJSONObject("Valute");

        Map<String, Object> fullMap = JsonFmUrl.jsonToMap(jsonCurrenciesOnly);

        Map<String, Float> rateOnlyMap = JsonFmUrl.getSimpleMap(fullMap);

        Float parsedFloat = rateOnlyMap.get("USD");
        Float parsedFloat1 = rateOnlyMap.get("доллар сша");
        System.out.println("Курс USD = " + parsedFloat);
        System.out.println("EUR = " + parsedFloat1);

    }
}