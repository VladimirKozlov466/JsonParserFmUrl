import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {

        JSONObject parsedFromUrl = JsonFmUrl.getJsonByUrl();

        JSONObject jsonCurrenciesOnly = parsedFromUrl.getJSONObject("Valute");

        String timeDate = parsedFromUrl.get("Timestamp").toString();

        Map<String, Object> fullMap = JsonFmUrl.jsonToMap(jsonCurrenciesOnly);

        Map<String, Float> rateOnlyMap = JsonFmUrl.getSimpleMap(fullMap);


        Scanner in = new Scanner(System.in);
        System.out.println("Введите название валюты, чтобы узнать ее курс по отношению RUR " +
                "в формате кода валюты (Например QAR) или название (например Китайский юань или доллар или Рупий)");

        String currencyName = in.nextLine();

        Map<String, Float> matchedCurrencies = JsonFmUrl.matchedCurrenciesMap(rateOnlyMap, currencyName);


        JsonFmUrl.printFoundCurrencies(matchedCurrencies);

        System.out.println("Данные Центробанка на " + timeDate);

        JsonFmUrl.writeFoundMatchesToExcel(matchedCurrencies);

    }
}