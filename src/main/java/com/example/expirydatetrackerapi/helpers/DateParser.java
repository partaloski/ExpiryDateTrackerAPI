package com.example.expirydatetrackerapi.helpers;

import java.time.LocalDate;

public class DateParser {
    public static LocalDate parseDate(String date){
        String [] parts = date.split("/");
        if(parts.length != 3)
            parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
    }
}
