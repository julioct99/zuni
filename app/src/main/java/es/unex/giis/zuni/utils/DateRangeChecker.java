package es.unex.giis.zuni.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRangeChecker {
    public Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public Date substractDays(Date date, int days){
        return addDays(date, -days);
    }

    public boolean dateWithinDays(Date date, int days){
        // Fecha actual + n dias
        Calendar targetDate = Calendar.getInstance();
        targetDate.setTime(new Date());
        targetDate.add(Calendar.DATE, days);

        // Fecha introducida
        Calendar inputDate = Calendar.getInstance();
        inputDate.setTime(date);

        return targetDate.after(inputDate);
    }
}
