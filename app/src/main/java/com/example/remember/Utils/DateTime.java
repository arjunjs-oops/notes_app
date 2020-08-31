package com.example.remember.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  static String getDateTime() {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy h a");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime myDateObj = LocalDateTime.now();
            return myDateObj.format(myFormatObj);
        }
        else{
            DateTime dateTime = new DateTime();
            return dateTime.toString();
        }


    }
}
