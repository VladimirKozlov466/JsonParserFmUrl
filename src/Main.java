import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class Main {

    public static String timeDate;
    public static void main(String[] args) {

        //Парсим js файл с курсами валют Центробанка России по url  в JSON объект
        JSONObject parsedFromUrl = JsonFmUrl.getJsonByUrl();

        //По ключу (атрибуту) "Valute" получаем значение в нашем случае новый JSON объект, содержащий только валюты
        JSONObject jsonCurrenciesOnly = parsedFromUrl.getJSONObject("Valute");

        //По ключу (атрибуту) "Timestamp" получаем значение времени, публикации курсов Центробанком
        timeDate = parsedFromUrl.get("Timestamp").toString();

        /* Парсим в HashMap JSON объект c курсами валют, где ключ это код валюты (например "USD")
        а значение это объект с атрибутами валюты*/
        Map<String, Object> fullMap = JsonFmUrl.jsonToMap(jsonCurrenciesOnly);

        /*Парсим в новую HashMap только курсы валют, где ключ типа String, содержащий код валюты (например "USD")
        или наименование валюты на русском языке типа "Туркменский манат"
         */
        Map<String, Float> rateOnlyMap = JsonFmUrl.getSimpleMap(fullMap);

        mainMenu(rateOnlyMap);

    }
    public static void mainMenu(Map<String, Float> rateOnlyMap) {
        Scanner in = new Scanner(System.in);
        int selectOption;
        System.out.println("""
                Выберете пункт меню
                1 - Найти и показать текущий курс валюты
                2 - Найти курс валюты, покзать и сохранить в Excel файл
                3 - Выход из программы""");

        selectOption = in.nextInt();

        switch (selectOption) {
            case 1:
                System.out.println("Введите название валюты, чтобы узнать ее курс по отношению RUB " +
                        "в формате кода валюты (Например QAR) или название (например Китайский юань" +
                        " или доллар или Рупий и тп)");
                Scanner newIn = new Scanner(System.in);
                String currencyName = newIn.nextLine();
                Map<String, Float> matchedCurrencies = JsonFmUrl.matchedCurrenciesMap(rateOnlyMap, currencyName);
                JsonFmUrl.printFoundCurrencies(matchedCurrencies);
                System.out.println("Данные Центробанка на " + timeDate);
                mainMenu(rateOnlyMap);
            case 2:
                System.out.println("Введите название валюты, чтобы узнать ее курс по отношению RUB " +
                        "в формате кода валюты (Например QAR) или название (например Китайский юань" +
                        " или доллар или Рупий и тп)");
                Scanner newNewIn = new Scanner(System.in);
                String currencyName1 = newNewIn.nextLine();
                Map<String, Float> matchedCurrencies1 = JsonFmUrl.matchedCurrenciesMap(rateOnlyMap, currencyName1);
                JsonFmUrl.printFoundCurrencies(matchedCurrencies1);
                System.out.println("Данные Центробанка на " + timeDate);
                try {
                    JsonFmUrl.writeFoundMatchesToExcel(matchedCurrencies1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mainMenu(rateOnlyMap);
            case 3:
                System.exit(0);
            default:
                System.out.println("Введено не корректное значение");
                mainMenu(rateOnlyMap);
        }
    }
}