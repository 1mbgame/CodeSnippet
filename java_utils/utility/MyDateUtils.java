package com.ngwisefood.app.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateUtils {

    private static final MyDateUtils ourInstance = new MyDateUtils();

    public static MyDateUtils getInstance() {
        return ourInstance;
    }

    private MyDateUtils() {
        // initialize
    }

    private Date originalStartDate;
    private long currentDateValue;

    public SimpleDateFormat yyyyDateFormatter = new SimpleDateFormat("yyyy");
    public SimpleDateFormat yyyyMMddDateFormatter = new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat ddMMEDateFormatter = new SimpleDateFormat("dd MMM E");
    public SimpleDateFormat yyyyMMDateFormatter = new SimpleDateFormat("yyyyMM");
    public SimpleDateFormat fullDateFormatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");


    public Date startOfDay(Date date){
        String dateString = yyyyMMddDateFormatter.format(date);
        dateString += " 00:00:01.000";

        try{
            Date startDate = fullDateFormatter.parse(dateString);
            return startDate;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Date endOfDay(Date date){
        String dateString = yyyyMMddDateFormatter.format(date);
        dateString += " 23:59:59.999";

        try{
            Date startDate = fullDateFormatter.parse(dateString);
            return startDate;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public Date startOfMonth(Date date){

        String dateString = yyyyMMDateFormatter.format(date);
        dateString += "01 00:00:01.000";

        try{
            Date startDate = fullDateFormatter.parse(dateString);
            return startDate;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public Date endOfMonth(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String dateString = yyyyMMDateFormatter.format(date);
        dateString += (day + " 23:59:59.999");

        try{
            Date startDate = fullDateFormatter.parse(dateString);
            return startDate;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Date startOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dateString = yyyyDateFormatter.format(date);
        dateString += "0101 00:00:01.000";

        try{
            Date startDate = fullDateFormatter.parse(dateString);
            return startDate;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Date endOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dateString = yyyyDateFormatter.format(date);
        dateString += "1231 23:59:59.999";

        try{
            Date endDate = fullDateFormatter.parse(dateString);
            return endDate;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public long getCurrentDateMilliSec(){
        currentDateValue = System.currentTimeMillis();
        return currentDateValue;
    }

    public Date getOriginalStartDate(){
        if(originalStartDate == null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                originalStartDate = simpleDateFormat.parse("1986-10-16 10:10:10.000");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return originalStartDate;
    }

    public int getDateNumber(Date date){
        String dateNumberString = yyyyMMddDateFormatter.format(date);
        return Integer.parseInt(dateNumberString);
    }

}
