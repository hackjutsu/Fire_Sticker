package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class ItemFactory {
    //TODO: Refactor ItemFactory to be a Proxy to SQLite Database

    static final private String TAG = "MEMORY-ACC";

    private static ArrayList<ArrayList<Item>> _itemLists = new ArrayList<>();
    private static ArrayList<String> _itemGroupList = new ArrayList<>();
    private static ArrayList<Group> _itemGroupObjectList = new ArrayList<>();
    private static ArrayList<Integer> _selectedItemIndexes = new ArrayList<>();

    public static Item createItem(int groupId, String frontSide, String backSide, String title, int bookMark) {

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

        // Notify Database the item creation
        notifyItemCreation(groupId, newItem);

        return newItem;
    }

    public static ArrayList<Item> createGroup(String groupName) {

        ArrayList<Item> newItemList = new ArrayList<>();

        _itemLists.add(newItemList);
        _itemGroupList.add(groupName);
        _itemGroupObjectList.add(new Group(groupName));
        _selectedItemIndexes.add(Integer.valueOf(-1));

        // Notify Database the item creation
        notifyGroupCreation(groupName);

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
            Log.d(TAG, "Invalid group Id! " + String.valueOf(groupId));
            Log.d(TAG, "_itemLists.size() " + String.valueOf(_itemLists.size()));

        }

        return _itemLists.get(groupId);
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

    public static ArrayList<Group> getItemGroupObjectList() {
        return _itemGroupObjectList;
    }

    public static ArrayList<String> getItemGroupObjectNameList() {

        ArrayList<String> groupNameArray = new ArrayList<>();

        for(int i=0;i<_itemGroupObjectList.size();++i) {
            groupNameArray.add(_itemGroupObjectList.get(i).getGroupName());
        }

        return groupNameArray;
    }



    public static void setSelectedItemIndex(int groupId, int selectedItemIndex) {

        if (groupId < 0 || groupId >= _selectedItemIndexes.size()) {
            Log.d(TAG, "Invalid group Id!");
        }

        _selectedItemIndexes.set(groupId, selectedItemIndex);
    }

    public static void notifyItemCreation(int grouId, Item newItem) {

    }

    public static void notifyItemDeletion(int grouId, Item deletedItem) {

    }

    public static void notifyGroupCreation(String newGroupName) {
    //TODO: Optimization with groupName and groupId is expected here
    }

    public static void notifyGroupDeletion(int grouId) {
    //TODO: Optimization with groupName and groupId is expected here
    }
}
