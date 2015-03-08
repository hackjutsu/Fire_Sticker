package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import android.util.Log;

import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.GroupsTableHelper;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.ItemsTableHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemFactory {
    //TODO: (DONE) Refactor ItemFactory to be a Proxy to SQLite Database

    static final private String TAG = "MEMORY-ACC";

    private static ArrayList<ArrayList<Item>> _itemLists = null;
    private static ArrayList<String> _itemGroupList = new ArrayList<>();
    private static ArrayList<Group> _itemGroupObjectList = null;
    private static ArrayList<Integer> _selectedItemIndexes = null;

    private static ItemsTableHelper _itemsTableHelper;
    private static GroupsTableHelper _groupsTableHelper;

    public static void setItemsTableHelper(ItemsTableHelper itemsTableHelper) {
        _itemsTableHelper = itemsTableHelper;
    }

    public static void setGroupsTableHelper(GroupsTableHelper groupsTableHelper) {
        _groupsTableHelper = groupsTableHelper;
    }

    public static Item createItem(int groupId, String frontSide, String backSide, String title, int bookMark, int stamp) {

        if (groupId < 0 || groupId >= _itemLists.size()) {
//            Log.d(TAG, "Invalid group Id!");
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


        Item newItem = new Item(frontSide, backSide, realTitle, bookMark, stamp);
        itemList.add(0, newItem);

        // Notify Database the item creation
        notifyItemCreation(getItemGroupObjectList().get(groupId), newItem);

        return newItem;
    }

    public static ArrayList<ArrayList<Item>> getItemLists() {

        if (_itemLists == null) {
//            Log.d(TAG, "_itemLists retrieve data from dataBase");
            _itemLists = new ArrayList<ArrayList<Item>>();
            _itemGroupObjectList = getItemGroupObjectList();

            for (int i = 0; i < _itemGroupObjectList.size(); ++i) {

                String groupUUID = _itemGroupObjectList.get(i).getUuid();
                ArrayList<Item> newItemList = _itemsTableHelper.getItemList(groupUUID);
                _itemLists.add(newItemList);
//                Log.d(TAG, _itemGroupObjectList.get(i).getGroupName() + ": " + newItemList.size());

            }
        }

        return _itemLists;
    }

    public static ArrayList<Item> createGroup(String groupName) {

        ArrayList<Item> newItemList = new ArrayList<>();

        _itemLists = getItemLists();
        _selectedItemIndexes = getSelectedItemIndexesList();

        _itemLists.add(newItemList);
        _itemGroupList.add(groupName);

        Group newGroup = new Group(groupName);
        _itemGroupObjectList.add(newGroup);
        _selectedItemIndexes.add(Integer.valueOf(-1));

        // Notify Database the item creation
        notifyGroupCreation(newGroup);

        return newItemList;
    }

    public static ArrayList<Integer> getSelectedItemIndexesList() {

        if (_selectedItemIndexes==null) {
            _selectedItemIndexes = new ArrayList<>();

            _itemGroupObjectList = getItemGroupObjectList();

            for (int i = 0; i < _itemGroupObjectList.size(); ++i) {
                _selectedItemIndexes.add(-1);
//                Log.d(TAG, String.valueOf(i));
            }

        }

//        Log.d(TAG, "_selectedItemIndexes.size() = " + String.valueOf(_selectedItemIndexes.size()));
        return _selectedItemIndexes;
    }

    public static ArrayList<Item> getItemList(int groupId) {

        _itemLists = getItemLists();

        if (groupId < 0 || groupId >= _itemLists.size()) {
//            Log.d(TAG, "Invalid group Id! " + String.valueOf(groupId));
//            Log.d(TAG, "_itemLists.size() " + String.valueOf(_itemLists.size()));

        }

        return _itemLists.get(groupId);
    }

    public static int getSelectedItemIndex(int groupId) {

        getSelectedItemIndexesList();

        if (groupId < 0 || groupId >= _selectedItemIndexes.size()) {
//            Log.d(TAG, "Invalid group Id!");
        }

        return _selectedItemIndexes.get(groupId);
    }

    public static ArrayList<String> getItemGroupList() {
        return _itemGroupList;
    }

    public static ArrayList<Group> getItemGroupObjectList() {

        if (_itemGroupObjectList == null) {
            _itemGroupObjectList = _groupsTableHelper.getAllGroups();
//            Log.d(TAG, "Retrieve Group List from DataBase: " + _itemGroupObjectList.size());
        }

        return _itemGroupObjectList;
    }

    public static ArrayList<String> getItemGroupObjectNameList() {

        ArrayList<String> groupNameArray = new ArrayList<>();

        _itemGroupObjectList = getItemGroupObjectList();

        for (int i = 0; i < _itemGroupObjectList.size(); ++i) {
            groupNameArray.add(_itemGroupObjectList.get(i).getGroupName());
        }

        return groupNameArray;
    }


    public static void setSelectedItemIndex(int groupId, int selectedItemIndex) {

        getSelectedItemIndexesList();

        if (groupId < 0 || groupId >= _selectedItemIndexes.size()) {
//            Log.d(TAG, "Invalid group Id!");
        }

        _selectedItemIndexes.set(groupId, selectedItemIndex);
    }

    public static void notifyItemCreation(final Group group, final Item newItem) {

//        Log.d(TAG, "notifyItemCreation");
        Runnable runnable = new Runnable() {
            public void run() {
                _itemsTableHelper.addItem(group, newItem);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    public static void notifyItemUpdate(final Group group, final Item item) {

//        Log.d(TAG, "notifyItemUpdate");
        _itemsTableHelper.updateItem(group, item);


//        Runnable runnable = new Runnable() {
//            public void run() {
//                _itemsTableHelper.updateItem(group, item);
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();

    }

    public static void notifyItemDeletion(final Item item) {

//        Log.d(TAG, "notifyItemDeletion");
        Runnable runnable = new Runnable() {
            public void run() {
                _itemsTableHelper.deleteItem(item);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    public static void notifyGroupCreation(final Group newGroup) {

//        _groupsTableHelper.addGroup(new Group(newGroupName));
//        Log.d(TAG, "notifyGroupCreation: " + newGroup.getUuid() + ", " + newGroup.getGroupName());

        Runnable runnable = new Runnable() {
            public void run() {
                _groupsTableHelper.addGroup(newGroup);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    public static void notifyGroupUpdate(final Group updatedGroup) {
//        Log.d(TAG, "notifyGroupUpdate: " + updatedGroup.getUuid() + ", " + updatedGroup.getGroupName());

        Runnable runnable = new Runnable() {
            public void run() {
                _groupsTableHelper.updateGroup(updatedGroup);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    public static void notifyGroupDeletion(final String uuid) {
//        Log.d(TAG, "notifyGroupDeletion: " + uuid);

        Runnable runnable = new Runnable() {
            public void run() {
                _groupsTableHelper.deleteGroup(uuid);
                _itemsTableHelper.deleteGroupItems(uuid);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    public static void closeAllDatabase() {
//        Log.d(TAG, "XX--closeAllDatabase--XX");
        _groupsTableHelper.close();
        _itemsTableHelper.close();
    }
}
