package com.gogocosmo.cosmoqiu.fire_sticker.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomizedTime {

    static public String getTimeStamp() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        String formattedTime = dateFormat.format(c.getTime());

        return formattedTime;
    }
}
