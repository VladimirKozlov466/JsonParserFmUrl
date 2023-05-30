import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFmUrl {

    private static final String TAG_PATH = "src\\example3.xlsx";
    private static final String TAG_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    // Метод для записи HashMap типа Map<String, Float> в Excel файл
    public static void writeFoundMatchesToExcel(Map<String, Float> stringFloatMap) throws IOException{
        XSSFWorkbook workbook;
        workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Курсы валют");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Наименование валюты");
        headerRow.createCell(1).setCellValue("Курс к RUB");

        int rowNum = 1;

        for (Map.Entry<String, Float> entry : stringFloatMap.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }

        FileOutputStream file = new FileOutputStream(TAG_PATH);
        workbook.write(file);
        file.close();
        System.out.println("Список валют сохранен в файл " + TAG_PATH);
    }
    /* Метод для поиска ключей в HashMap типа Map<String, Float>, частично совпадающих со значением искомой строки,
    и запись подходящихпар ключ - значение в новую HashMap типа Map<String, Float>
     */
    public static Map<String, Float> matchedCurrenciesMap (Map<String, Float> stringFloatMap, String searchingKey) {

        Map<String, Float> matchedCurrencies = new HashMap<>();
        String searchKey = searchingKey.toLowerCase().replace("\n", "").trim();
        String searchUpperValues = searchKey.toUpperCase();

        for (Map.Entry<String, Float> entry : stringFloatMap.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            if (key.contains(searchUpperValues)) {
                matchedCurrencies.put(key, value);
            } else if (key.contains(searchKey)) {
                matchedCurrencies.put(key, value);
            }
        }
        return matchedCurrencies;
    }
    // Метод для консольного вывода пар ключ - значение из HashMap типа Map<String, Float>
    public static void printFoundCurrencies (Map<String, Float> stringFloatMap) {
        if (stringFloatMap.size() > 0) {
            System.out.println("Возможно вы искали:");
            stringFloatMap.forEach((key, value) -> System.out.println("Курс " + key + " = " + value + " RUB"));
        } else {
            System.out.println("Такой валюты нет в списке. Проверьте название");
        }
    }
    /* Метод для поиска значения искомог атрибута в HashMap типа Map<String, Object> и запись
    пары ключ - значение атрибута в новую HashMap типа Map<String, Float>
     */
    public static Map<String, Float> getSimpleMap (Map<String, Object> map) {
        Map<String, Float> rateOnlyMap = new HashMap<>();

        for (String key: map.keySet()) {
            float currentRate = 0;
            String additionalKey;
            String currencyToString = map.get(key).toString();
            List<String> currencyToList = new ArrayList<>(Arrays.asList(currencyToString.toLowerCase()
                    .replace("{", "").replace("}", "").split(",")));
            for (String s : currencyToList) {

                if (s.contains("value=")) {
                    currentRate = Float.parseFloat(s.replace("value=", ""));

                } else if (s.contains("name=")) {
                    additionalKey = s.replace("name=", "").trim();
                    rateOnlyMap.put(additionalKey, currentRate);
                }
            }
            rateOnlyMap.put(key, currentRate);


        }
        return rateOnlyMap;
    }
    // Метод для для считывания JSON строки из js файла по URL в JSONObject
    public static JSONObject getJsonByUrl () throws JSONException {
        try {
            URL url = new URL(TAG_URL);
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
    // Метод для JSONObject в HashMap типа Map<String, Object>
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> valuteMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            valuteMap = toMap(json);
        }
        return valuteMap;
    }

    // Метод для JSONObject в HashMap типа Map<String, Object> с рекусивным вызовом
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

    // Метод для JSONArray в список типа List<Object>
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
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
