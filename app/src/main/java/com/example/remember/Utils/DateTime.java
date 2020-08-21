package com.example.remember.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  static String getDateTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
}
