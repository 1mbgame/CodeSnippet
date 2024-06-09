package com.ngwisefood.app.utility;

import com.google.gson.Gson;
import com.ngwisefood.app.constant.CountryCode;
import com.ngwisefood.app.constant.Language;
import com.ngwisefood.app.constant.SessionName;
import com.ngwisefood.app.model.GlobalData;
import jakarta.servlet.http.HttpSession;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AppUtil {

    private static String[] activeMenus = new String[10];

    public static Gson gson = new Gson();

    public static String[] getActiveMenus(int index){
        for (int i = 0; i < activeMenus.length; i++) {
            if(index == i){
                activeMenus[i] = "active";
            }else{
                activeMenus[i] = "";
            }
        }
        return activeMenus;
    }

    public static String getLanguageCode(String language){
        if(language.equalsIgnoreCase("en")){
            return Language.EN;
        }else if(language.equalsIgnoreCase("cn")){
            return Language.CN;
        }else if(language.equalsIgnoreCase("my")){
            return Language.MY;
        }else{
            return Language.EN;
        }
    }

    public static String getLanguageCode(HttpSession httpSession) {
        Object objectLanguage = httpSession.getAttribute(SessionName.LANGUAGE_CODE);

        String language = "";
        if (objectLanguage == null) {
            language = Language.EN;
            httpSession.setAttribute(SessionName.LANGUAGE_CODE,Language.EN);
        }else{
            language = objectLanguage.toString();
        }

        return language;
    }

    public static String getLastPage(HttpSession httpSession){

        String lastPage = (String)httpSession.getAttribute(SessionName.LAST_PAGE);
        if(lastPage == null){
            lastPage = "/";
            httpSession.setAttribute(SessionName.LAST_PAGE,lastPage);
        }

        return lastPage;

    }

    public static String getAvaDayDisplayList(String languageCode,List<Integer> avaDayList) {
        Map<String,String> languageMap = GlobalData.getInstance().languageCodeMap.get(languageCode);
        List<String> avaDaydisplayList = new ArrayList<>();
        int index = 0;
        for(Integer integer : avaDayList){
            switch (index){
                case 0 : if(integer == 1) avaDaydisplayList.add(languageMap.get("monday"));break;
                case 1 : if(integer == 1) avaDaydisplayList.add(languageMap.get("tuesday"));break;
                case 2 : if(integer == 1) avaDaydisplayList.add(languageMap.get("wednesday"));break;
                case 3 : if(integer == 1) avaDaydisplayList.add(languageMap.get("thursday"));break;
                case 4 : if(integer == 1) avaDaydisplayList.add(languageMap.get("friday"));break;
                case 5 : if(integer == 1) avaDaydisplayList.add(languageMap.get("saturday"));break;
                case 6 : if(integer == 1) avaDaydisplayList.add(languageMap.get("sunday"));break;

                default:avaDaydisplayList.add(languageMap.get("monday"));break;
            }
            index += 1;
        }
        String avaDayString = "-";
        if(avaDaydisplayList.size() > 0){
            if(avaDaydisplayList.size() >= 7){
                avaDayString = languageMap.get("every_day");
            }else{
                avaDayString = avaDaydisplayList.toString();
                avaDayString = avaDayString.replace("[","").replaceAll("]","");
            }
        }

        return avaDayString;
    }

    public static String getCountryCode(HttpSession httpSession) {
        Object objectCountryCode = httpSession.getAttribute(SessionName.COUNTRY_CODE);

        String countryCode = "";
        if (objectCountryCode == null) {
            countryCode = CountryCode.MALAYSIA;
            httpSession.setAttribute(SessionName.COUNTRY_CODE, countryCode);
        }else{
            countryCode = objectCountryCode.toString();
        }

        return countryCode;
    }


    private static SimpleDateFormat jobRowDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
    public static String getDisplayDate(long longDate){

        Date date = new Date(longDate);
        String dateString = jobRowDateFormat.format(date);

        return dateString;

    }

    private static NumberFormat formatter = new DecimalFormat("#0.00");
    private static Integer currentDecimal = 0;
    public static String formatDecimal(Double value, Integer decimal){
        if(currentDecimal != decimal){
            currentDecimal = decimal;
            String totalDecimal = "";
            for (int i = 0; i < decimal; i++) {
                totalDecimal += "0";
            }
            formatter = new DecimalFormat("#0." + totalDecimal);
        }

        String formattedValue = formatter.format(value);

        return formattedValue;
    }

    public static Double roundDecimal(Double value, Integer decimal){
        String newValue = formatDecimal(value,decimal);
        try{
            Double dblValue = Double.parseDouble(newValue);
            return dblValue;
        }catch (Exception e){
            //e.printStackTrace();
        }

        return 0.0;
    }





}
