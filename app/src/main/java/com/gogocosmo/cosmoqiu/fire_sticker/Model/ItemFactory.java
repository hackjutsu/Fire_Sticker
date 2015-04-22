package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedTime;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemFactory {

    static final private String TAG = "MEMORY-ACC";

    private static ArrayList<ArrayList<Item>> mItemLists = null;
    private static ArrayList<Group> mItemGroupObjectList = null;
    private static ArrayList<Integer> mSelectedItemIndexes = null;

    private static DatabaseHelper mDatabaseHelper;

    public static void setItemsTableHelper(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    public static Item createItem(int groupId, String frontSide, String backSide, String title, int bookMark, int stamp, int lock) {

        if (groupId < 0 || groupId >= mItemLists.size()) {
            return null;
        }

        ArrayList<Item> itemList = mItemLists.get(groupId);

        String realTitle = title;
        if (realTitle == "") {

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_WEEK);
            String weekDay = "Sun";

            switch (day) {
                case Calendar.SUNDAY:
                    weekDay = "Sun";
                    break;
                case Calendar.MONDAY:
                    weekDay = "Mon";
                    break;
                case Calendar.TUESDAY:
                    weekDay = "Tue";
                    break;
                case Calendar.WEDNESDAY:
                    weekDay = "Wed";
                    break;
                case Calendar.THURSDAY:
                    weekDay = "Thur";
                    break;
                case Calendar.FRIDAY:
                    weekDay = "Fri";
                    break;
                case Calendar.SATURDAY:
                    weekDay = "Sat";
                    break;
                default:
            }

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = df.format(c.getTime());

            realTitle = weekDay + ", " + formattedDate;
        }

        Item newItem = new Item(frontSide, backSide, realTitle, bookMark, stamp, lock);
        newItem.setDateCreation(CustomizedTime.getTimeStamp());
        newItem.setDateUpdate(CustomizedTime.getTimeStamp());
        itemList.add(0, newItem);

        // Notify Database the item creation
        notifyItemCreation(getItemGroupObjectList().get(groupId), newItem);

        return newItem;
    }

    public static ArrayList<ArrayList<Item>> getItemLists() {

        if (mItemLists == null) {
            mItemLists = new ArrayList<>();
            mItemGroupObjectList = getItemGroupObjectList();

            for (int i = 0; i < mItemGroupObjectList.size(); ++i) {

                String groupUUID = mItemGroupObjectList.get(i).getUuid();
                ArrayList<Item> newItemList = mDatabaseHelper.getItemList(groupUUID);
                mItemLists.add(newItemList);
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
            }

        }
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
            mItemGroupObjectList = mDatabaseHelper.getAllGroups();
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

    public static void notifyDBRestore() {
        mItemLists = null;
        mItemGroupObjectList = null;
        mSelectedItemIndexes = null;
    }

    public static void notifyItemCreation(final Group group, final Item newItem) {

//        Log.d(TAG, "notifyItemCreation");
        mDatabaseHelper.addItem(group, newItem);
    }

    public static void notifyItemUpdate(final Group group, final Item item) {

//        Log.d(TAG, "notifyItemUpdate");
        mDatabaseHelper.updateItem(group, item);
    }

    public static void notifyItemDeletion(final Item item) {

//        Log.d(TAG, "notifyItemDeletion");
        mDatabaseHelper.deleteItem(item);
    }

    public static void notifyGroupCreation(final Group newGroup) {

//        Log.d(TAG, "notifyGroupCreation: " + newGroup.getUuid() + ", " + newGroup.getGroupName());
        mDatabaseHelper.addGroup(newGroup);
    }

    public static void notifyGroupUpdate(final Group updatedGroup) {

//        Log.d(TAG, "notifyGroupUpdate: " + updatedGroup.getUuid() + ", " + updatedGroup.getGroupName());
        mDatabaseHelper.updateGroup(updatedGroup);
    }

    public static void notifyGroupDeletion(final String uuid) {

//        Log.d(TAG, "notifyGroupDeletion: " + uuid);
        mDatabaseHelper.deleteGroup(uuid);
        mDatabaseHelper.deleteGroupItems(uuid);
    }

    public static void closeAllDatabase() {

//        mDatabaseHelper.close();
    }
}
