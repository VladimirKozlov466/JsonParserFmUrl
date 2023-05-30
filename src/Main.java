import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


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


        Scanner in = new Scanner(System.in);
        System.out.println("Введите название валюты, чтобы узнать ее курс по отношению RUR " +
                "в формате кода валюты (Например QAR) или название (например Китайский юань)");

        String currencyName = in.nextLine();
        String searchKey = currencyName.toLowerCase().replace("\n", "").trim();
        String searchUpperValues = searchKey.toUpperCase();
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

        Map<String, Float> matchedCurrencies = new HashMap<>();

        for (Map.Entry<String, Float> entry : rateOnlyMap.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            if (key.contains(searchUpperValues)) {
//                System.out.println("Курс " + key + " = " + value);
                matchedCurrencies.put(key, value);
            } else if (key.contains(searchKey)) {
//                System.out.println("Курс " + key + " = " + value);
                matchedCurrencies.put(key, value);
            }
        }
        if (matchedCurrencies.size() > 0) {
            System.out.println("Возможно вы искали:");
            matchedCurrencies.forEach((key, value) -> System.out.println("Курс " + key + " = " + value));
        } else {
            System.out.println("Такой валюты нет в списке. Проверьте название");
        }

        XSSFWorkbook workbook;
        workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Курсы валют");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Наименование валюты");
        headerRow.createCell(1).setCellValue("Курс к RUB");

        int rowNum = 1;

        for (Map.Entry<String, Float> entry : matchedCurrencies.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) entry.getKey());
            row.createCell(1).setCellValue((Float) entry.getValue());
        }

        try {
            FileOutputStream file = new FileOutputStream("src\\example3.xlsx");
            workbook.write(file);
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
//        Float parsedFloat = rateOnlyMap.get("USD");
//        Float parsedFloat1 = rateOnlyMap.get("доллар сша");
//        System.out.println("Курс USD = " + parsedFloat);
//        System.out.println("доллар сша = " + parsedFloat1);

    }
}