import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFmUrl {

//    public static void main(String[] args) {
//
//
//        JSONObject parsedFromUrl;
//
//        {
//            try {
//                parsedFromUrl = getJsonByUrl();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        JSONObject jsonCurrenciesOnly = parsedFromUrl.getJSONObject("Valute");
//
//        Map<String, Object> fullMap = jsonToMap(jsonCurrenciesOnly);
//
//        Map<String, Float> rateOnlyMap = getSimpleMap(fullMap);
//
//
////        String url1 = "https://www.cbr-xml-daily.ru/daily_json.js";
////        try {
////            URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
////            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////            conn.setRequestMethod("GET");
////            conn.setRequestProperty("Accept", "application/json");
////
////            if (conn.getResponseCode() != 200) {
////                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
////            }
////
////            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
////
////            String output;
////            StringBuilder builder = new StringBuilder();
////            while ((output = br.readLine()) != null) {
////                builder.append(output);
////            }
////
////            conn.disconnect();
////
////            JSONObject json = new JSONObject(builder.toString());
//////            System.out.println(json);
////            JSONObject jsonValute = (JSONObject) json.get("Valute");
////            System.out.println(jsonValute);
////
////            Map<String, Object> map = jsonToMap(jsonValute);
////
////            Map<String, Float> currencyMap = new HashMap<>();
//
////        for (String key: fullMap.keySet()) {
////            float currentRate = 0;
////            String additionalKey = "";
////            String currencyToString = fullMap.get(key).toString();
////            List<String> currencyToList = new ArrayList<>(Arrays.asList(currencyToString.toLowerCase()
////                    .replace("{", "").replace("}", "").split(",")));
////            for (int i = 0; i < currencyToList.size(); i++) {
////
////                if(currencyToList.get(i).contains("value=")) {
////                    currentRate = Float.parseFloat(currencyToList.get(i).replace("value=", ""));
////
////                } else if (currencyToList.get(i).contains("name=")) {
////                    additionalKey = currencyToList.get(i).replace("name=", "").trim();
////                    rateOnlyMap.put(additionalKey, currentRate);
////                }
////            }
////            rateOnlyMap.put(key, currentRate);
////
////
////        }
//
//        Float parsedFloat = rateOnlyMap.get("USD");
//        Float parsedFloat1 = rateOnlyMap.get("доллар сша");
//        System.out.println("Курс USD = " + parsedFloat);
//        System.out.println("EUR = " + parsedFloat1);
//
//
//
//    }

    public static Map<String, Float> getSimpleMap (Map<String, Object> map) {
        Map<String, Float> rateOnlyMap = new HashMap<>();

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
                    additionalKey = currencyToList.get(i).replace("name=", "").trim();
                    rateOnlyMap.put(additionalKey, currentRate);
                }
            }
            rateOnlyMap.put(key, currentRate);


        }
        return rateOnlyMap;
    }

    public static JSONObject getJsonByUrl () throws IOException, JSONException {
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

            return new JSONObject(builder.toString());
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException(e);
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

    public static void showSearchResult (Map<String, Float> stringFloatMap, String keySearched) {
        String searchKey = keySearched.toLowerCase().replace("\n", "").trim();
        String searchUpperValues = searchKey.toUpperCase();
        if (stringFloatMap.containsKey(searchKey)) {
            System.out.println("Курс " + keySearched + " = " + stringFloatMap.get(searchKey));
        }
        else if (stringFloatMap.containsKey(searchUpperValues)){
            System.out.println("Курс " + searchUpperValues + " = " + stringFloatMap.get(searchUpperValues));
        }
        else {
            System.out.println("Такой валюты нет в списке. Проверьте название");
        }
    }
}
