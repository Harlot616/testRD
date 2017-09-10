package test.fugga.com.myapplication.Classes;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SortUtil {
    public ArrayList<MyData> QuickSort(ArrayList<MyData> data, int left, int right, boolean esMenorMayor) {
        MyData pivote;
        int le, ri;
        le = left;
        ri = right;
        pivote = data.get((left + right) / 2);

        do {
            if (esMenorMayor) {
                while (data.get(le).created_at.compareToIgnoreCase(pivote.created_at) < 0)
                    le++;
                while (data.get(ri).created_at.compareToIgnoreCase(pivote.created_at) > 0)
                    ri--;
            } else {
                while (data.get(le).created_at.compareToIgnoreCase(pivote.created_at) > 0)
                    le++;
                while (data.get(ri).created_at.compareToIgnoreCase(pivote.created_at) < 0)
                    ri--;
            }

            if (le <= ri) {
                MyData temp;
                temp = data.get(le);
                data.set(le, data.get(ri));
                data.set(ri, temp);
                le++;
                ri--;
            }
        } while (le <= ri);

        if (left < ri) {
            QuickSort(data, left, ri, esMenorMayor);
        }
        if (le < right) {
            QuickSort(data, le, right, esMenorMayor);
        }

        return data;
    }

    public String timePassed(String hitDate) {
        String elapsed;
        Date date = new Date();
        DateTime now = new DateTime(date);
        DateTime hit = ISODateTimeFormat.dateTimeParser().parseDateTime(hitDate);

        int seconds = Seconds.secondsBetween(hit, now).getSeconds();
        int minutes = Minutes.minutesBetween(hit, now).getMinutes();
        int hours = Hours.hoursBetween(hit, now).getHours();
        int days = Days.daysBetween(hit, now).getDays();

        if (days >= 7) {
            elapsed = hit.getDayOfMonth() + "/" + hit.getMonthOfYear() + "/" + hit.getYear();
        } else if (days < 7 && days > 0) {
            elapsed = days + "d";
        } else if (hours < 24) {
            elapsed = hours + "h";
        } else if(minutes < 60) {
            elapsed = minutes + "m";
        } else {
            elapsed = seconds + "s";
        }


        return elapsed;
    }
}
