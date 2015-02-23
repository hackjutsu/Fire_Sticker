package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class ItemFactory {

    private static ArrayList<Item> _itemList = new ArrayList<>();
    private static int _selectedItemIndex = -1;

    public static Item createItem(String question, String answer, String title, Boolean light) {

        String realTitle = title;
        if (realTitle == "") {

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_WEEK);
            String weekDay = "Sunday";

            switch (day) {
                case Calendar.SUNDAY:
                    weekDay = "Sunday";
                    break;
                case Calendar.MONDAY:
                    weekDay = "Monday";
                    break;
                case Calendar.TUESDAY:
                    weekDay = "Tuesday";
                    break;
                case Calendar.WEDNESDAY:
                    weekDay = "Wednesday";
                    break;
                case Calendar.THURSDAY:
                    weekDay = "Thursday";
                    break;
                case Calendar.FRIDAY:
                    weekDay = "Friday";
                    break;
                case Calendar.SATURDAY:
                    weekDay = "Saturday";
                    break;
                default:
            }

            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
            String formattedDate = df.format(c.getTime());

            realTitle = weekDay + ", " + formattedDate;
        }


        Item newItem = new Item(question, answer, realTitle, light);
        _itemList.add(0, newItem);
        return newItem;
    }

    public static ArrayList<Item> getItemList() {
        return _itemList;
    }

    public static void setSelectedItemIndex(int selectedItemIndex) {
        _selectedItemIndex = selectedItemIndex;
    }

    public static int getSelectedItemIndex() {
        return _selectedItemIndex;
    }
}
