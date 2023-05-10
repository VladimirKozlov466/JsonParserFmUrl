import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

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


//        Scanner in = new Scanner(System.in);
//        System.out.println("Введите название валюты, чтобы узнать ее курс по отношению RUR" +
//                "В формате кода валюты (Например QAR) или название (например Китайский юань)");
//
//        String currencyName = in.next();
//
//        if (rateOnlyMap.containsKey(currencyName)) {
//            System.out.println("Курс " + currencyName + " = " + rateOnlyMap.get(currencyName));
//        } else {
//            System.out.println("Такой валюты нет в списке. Проверьте название");
//        }

        Float parsedFloat = rateOnlyMap.get("USD");
        Float parsedFloat1 = rateOnlyMap.get("доллар сша");
        System.out.println("Курс USD = " + parsedFloat);
        System.out.println("доллар сша = " + parsedFloat1);

    }
}