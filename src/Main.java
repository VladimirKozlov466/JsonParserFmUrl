import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
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


        Scanner in = new Scanner(System.in);
        System.out.println("Введите название валюты, чтобы узнать ее курс по отношению RUR " +
                "в формате кода валюты (Например QAR) или название (например Китайский юань)");

        String currencyName = in.nextLine();
//        String searchKey = currencyName.toLowerCase().replace("\n", "").trim();
//        String searchUpperValues = searchKey.toUpperCase();
//
//        if (rateOnlyMap.containsKey(searchKey)) {
//            System.out.println("Курс " + currencyName + " = " + rateOnlyMap.get(searchKey));
//        }
//        else if (rateOnlyMap.containsKey(searchUpperValues)){
//            System.out.println("Курс " + searchUpperValues + " = " + rateOnlyMap.get(searchUpperValues));
//        }
//        else {
//            System.out.println("Такой валюты нет в списке. Проверьте название");
//        }
//        JsonFmUrl.showSearchResult(rateOnlyMap, currencyName);

//        Map<String, Float> matchedCurrencies = new HashMap<>();
//
//        for (Map.Entry<String, Float> entry : rateOnlyMap.entrySet()) {
//            String key = entry.getKey();
//            Float value = entry.getValue();
//            if (key.contains(searchUpperValues)) {
////                System.out.println("Курс " + key + " = " + value);
//                matchedCurrencies.put(key, value);
//            } else if (key.contains(searchKey)) {
////                System.out.println("Курс " + key + " = " + value);
//                matchedCurrencies.put(key, value);
//            }
//        }

        Map<String, Float> matchedCurrencies = JsonFmUrl.matchedCurrenciesMap(rateOnlyMap, currencyName);


        JsonFmUrl.printFoundCurrencies(matchedCurrencies);


        JsonFmUrl.writeFoundMatchesToExcel(matchedCurrencies);

//
//        Set<String> keysMatched = rateOnlyMap.keySet().stream()
//                .filter(k -> k.toLowerCase().contains(searchKey)).collect(Collectors.toSet());
//        Iterator<String> keys = keysMatched.iterator();
//        if (keysMatched.size() > 0) {
//            while (keys.hasNext()){
//                System.out.println("Курс " + keys.next() + " = " + rateOnlyMap.get(keys.next()));
//            }
//        } else {
//            System.out.println("Такой валюты нет в списке. Проверьте название");
//        }

    }
}