import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFmUrl {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }

            conn.disconnect();

            JSONObject json = new JSONObject(builder.toString());
//            System.out.println(json);
            JSONObject jsonValute = (JSONObject) json.get("Valute");
            System.out.println(jsonValute);

            Map<String, Object> map = jsonToMap(jsonValute);

            Map<String, Float> currencyMap = new HashMap<>();

            for (String key: map.keySet()) {
                float currentRate = 0;
                String additionalKey = "";
                String currencyToString = map.get(key).toString();
                List<String> currencyToList = new ArrayList<>(Arrays.asList(currencyToString.toLowerCase()
                        .replace("{", "").replace("}", "").split(",")));
                for (int i = 0; i < currencyToList.size(); i++) {

                    if(currencyToList.get(i).contains("value=")) {
                        currentRate = Float.parseFloat(currencyToList.get(i).replace("value=", ""));

                    } else if (currencyToList.get(i).contains("name=")) {
                        additionalKey = currencyToList.get(i).replace("name=", "");
                        currencyMap.put(additionalKey, currentRate);
                    }
                }
                currencyMap.put(key, currentRate);


            }

            Float parsedFloat = currencyMap.get("USD");
            Float parsedFloat1 = currencyMap.get("EUR");
            System.out.println("Курс USD = " + parsedFloat);
            System.out.println("EUR = " + parsedFloat1);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> valuteMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            valuteMap = toMap(json);
        }
        return valuteMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }

            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
