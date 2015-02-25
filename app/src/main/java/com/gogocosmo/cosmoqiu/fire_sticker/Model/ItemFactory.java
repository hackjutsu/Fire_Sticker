package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class ItemFactory {

    static final private String TAG = "MEMORY-ACC";

    private static ArrayList<ArrayList<Item>> _itemLists = new ArrayList<>();
    private static ArrayList<String> _itemGroupList = new ArrayList<>();
    private static ArrayList<Integer> _selectedItemIndexes = new ArrayList<>();

    public static Item createItem(int groupId, String frontSide, String backSide, String title, Boolean bookMark) {

        if (groupId < 0 || groupId >= _itemLists.size()) {
            Log.d(TAG, "Invalid group Id!");
            return null;
        }

        ArrayList<Item> itemList = _itemLists.get(groupId);

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

            SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
            String formattedDate = df.format(c.getTime());

            realTitle = weekDay + ", " + formattedDate;
        }


        Item newItem = new Item(frontSide, backSide, realTitle, bookMark);
        itemList.add(0, newItem);
        return newItem;
    }

    public static ArrayList<Item> createGroup(String groupName) {

        ArrayList<Item> newItemList = new ArrayList<>();

        _itemLists.add(newItemList);
        _itemGroupList.add(groupName);
        _selectedItemIndexes.add(Integer.valueOf(-1));

        return newItemList;
    }

    public static ArrayList<ArrayList<Item>> getItemLists() {

        return _itemLists;
    }

    public static ArrayList<Integer> getSelectedItemIndexesList() {
        return _selectedItemIndexes;
    }

    public static ArrayList<Item> getItemList(int groupId) {

        if (groupId < 0 || groupId >= _itemLists.size()) {
            Log.d(TAG, "Invalid group Id!");
        }

        return _itemLists.get(groupId);
    }

    public static void setSelectedGroupItemIndex(int groupId, int selectedItemIndex) {

        if (groupId < 0 || groupId >= _selectedItemIndexes.size()) {
            Log.d(TAG, "Invalid group Id!");
        }

        _selectedItemIndexes.set(groupId, selectedItemIndex);
    }

    public static int getSelectedItemIndex(int groupId) {

        if (groupId < 0 || groupId >= _selectedItemIndexes.size()) {
            Log.d(TAG, "Invalid group Id!");
        }

        return _selectedItemIndexes.get(groupId);
    }

    public static ArrayList<String> getItemGroupList() {
        return _itemGroupList;
    }
}
