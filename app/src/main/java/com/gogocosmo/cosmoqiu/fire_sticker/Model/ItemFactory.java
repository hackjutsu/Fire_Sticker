package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.GroupsTableHelper;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.ItemsTableHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemFactory {

    static final private String TAG = "MEMORY-ACC";

    private static ArrayList<ArrayList<Item>> mItemLists = null;
    private static ArrayList<Group> mItemGroupObjectList = null;
    private static ArrayList<Integer> mSelectedItemIndexes = null;

    private static ItemsTableHelper mItemsTableHelper;
    private static GroupsTableHelper mGroupsTableHelper;

    public static void setItemsTableHelper(ItemsTableHelper itemsTableHelper) {
        mItemsTableHelper = itemsTableHelper;
    }

    public static void setGroupsTableHelper(GroupsTableHelper groupsTableHelper) {
        mGroupsTableHelper = groupsTableHelper;
    }

    public static Item createItem(int groupId, String frontSide, String backSide, String title, int bookMark, int stamp) {

        if (groupId < 0 || groupId >= mItemLists.size()) {
//            Log.d(TAG, "Invalid group Id!");
            return null;
        }

        ArrayList<Item> itemList = mItemLists.get(groupId);

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

        if (mItemLists == null) {
//            Log.d(TAG, "mItemLists retrieve data from dataBase");
            mItemLists = new ArrayList<ArrayList<Item>>();
            mItemGroupObjectList = getItemGroupObjectList();

            for (int i = 0; i < mItemGroupObjectList.size(); ++i) {

                String groupUUID = mItemGroupObjectList.get(i).getUuid();
                ArrayList<Item> newItemList = mItemsTableHelper.getItemList(groupUUID);
                mItemLists.add(newItemList);
//                Log.d(TAG, mItemGroupObjectList.get(i).getGroupName() + ": " + newItemList.size());

            }
        }

        return mItemLists;
    }

    public static ArrayList<Item> createGroup(String groupName) {

        ArrayList<Item> newItemList = new ArrayList<>();

        mItemLists = getItemLists();
        mSelectedItemIndexes = getSelectedItemIndexesList();

        mItemLists.add(newItemList);

        Group newGroup = new Group(groupName);
        mItemGroupObjectList.add(newGroup);
        mSelectedItemIndexes.add(Integer.valueOf(-1));

        // Notify Database the item creation
        notifyGroupCreation(newGroup);

        return newItemList;
    }

    public static ArrayList<Integer> getSelectedItemIndexesList() {

        if (mSelectedItemIndexes == null) {
            mSelectedItemIndexes = new ArrayList<>();

            mItemGroupObjectList = getItemGroupObjectList();

            for (int i = 0; i < mItemGroupObjectList.size(); ++i) {
                mSelectedItemIndexes.add(-1);
//                Log.d(TAG, String.valueOf(i));
            }

        }

//        Log.d(TAG, "mSelectedItemIndexes.size() = " + String.valueOf(mSelectedItemIndexes.size()));
        return mSelectedItemIndexes;
    }

    public static ArrayList<Item> getItemList(int groupId) {

        mItemLists = getItemLists();

        if (groupId < 0 || groupId >= mItemLists.size()) {
//            Log.d(TAG, "Invalid group Id! " + String.valueOf(groupId));
//            Log.d(TAG, "mItemLists.size() " + String.valueOf(mItemLists.size()));

        }

        return mItemLists.get(groupId);
    }

    public static int getSelectedItemIndex(int groupId) {

        getSelectedItemIndexesList();

        if (groupId < 0 || groupId >= mSelectedItemIndexes.size()) {
//            Log.d(TAG, "Invalid group Id!");
        }

        return mSelectedItemIndexes.get(groupId);
    }

    public static ArrayList<Group> getItemGroupObjectList() {

        if (mItemGroupObjectList == null) {
            mItemGroupObjectList = mGroupsTableHelper.getAllGroups();
//            Log.d(TAG, "Retrieve Group List from DataBase: " + mItemGroupObjectList.size());
        }

        return mItemGroupObjectList;
    }

    public static ArrayList<String> getItemGroupObjectNameList() {

        ArrayList<String> groupNameArray = new ArrayList<>();

        mItemGroupObjectList = getItemGroupObjectList();

        for (int i = 0; i < mItemGroupObjectList.size(); ++i) {
            groupNameArray.add(mItemGroupObjectList.get(i).getGroupName());
        }

        return groupNameArray;
    }


    public static void setSelectedItemIndex(int groupId, int selectedItemIndex) {

        getSelectedItemIndexesList();

        if (groupId < 0 || groupId >= mSelectedItemIndexes.size()) {
//            Log.d(TAG, "Invalid group Id!");
        }

        mSelectedItemIndexes.set(groupId, selectedItemIndex);
    }

    public static void notifyItemCreation(final Group group, final Item newItem) {

//        Log.d(TAG, "notifyItemCreation");
        mItemsTableHelper.addItem(group, newItem);

//        Runnable runnable = new Runnable() {
//            public void run() {
//                mItemsTableHelper.addItem(group, newItem);
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();

    }

    public static void notifyItemUpdate(final Group group, final Item item) {

//        Log.d(TAG, "notifyItemUpdate");
        mItemsTableHelper.updateItem(group, item);


//        Runnable runnable = new Runnable() {
//            public void run() {
//                mItemsTableHelper.updateItem(group, item);
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();

    }

    public static void notifyItemDeletion(final Item item) {

//        Log.d(TAG, "notifyItemDeletion");
        mItemsTableHelper.deleteItem(item);

//        Runnable runnable = new Runnable() {
//            public void run() {
//                mItemsTableHelper.deleteItem(item);
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();
//
    }

    public static void notifyGroupCreation(final Group newGroup) {

//        mGroupsTableHelper.addGroup(new Group(newGroupName));
//        Log.d(TAG, "notifyGroupCreation: " + newGroup.getUuid() + ", " + newGroup.getGroupName());
        mGroupsTableHelper.addGroup(newGroup);


//        Runnable runnable = new Runnable() {
//            public void run() {
//                mGroupsTableHelper.addGroup(newGroup);
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();
    }

    public static void notifyGroupUpdate(final Group updatedGroup) {
//        Log.d(TAG, "notifyGroupUpdate: " + updatedGroup.getUuid() + ", " + updatedGroup.getGroupName());

        mGroupsTableHelper.updateGroup(updatedGroup);

//        Runnable runnable = new Runnable() {
//            public void run() {
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();

    }

    public static void notifyGroupDeletion(final String uuid) {
//        Log.d(TAG, "notifyGroupDeletion: " + uuid);
        mGroupsTableHelper.deleteGroup(uuid);
        mItemsTableHelper.deleteGroupItems(uuid);

//        Runnable runnable = new Runnable() {
//            public void run() {
//                mGroupsTableHelper.deleteGroup(uuid);
//                mItemsTableHelper.deleteGroupItems(uuid);
//            }
//        };
//        Thread mythread = new Thread(runnable);
//        mythread.start();
    }

    public static void closeAllDatabase() {
//        Log.d(TAG, "XX--closeAllDatabase--XX");
//        mGroupsTableHelper.close();
//        mItemsTableHelper.close();
    }
}
