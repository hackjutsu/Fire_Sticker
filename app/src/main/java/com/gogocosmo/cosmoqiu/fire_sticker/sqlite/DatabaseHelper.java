package com.gogocosmo.cosmoqiu.fire_sticker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.Group;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseHelper extends SQLiteOpenHelper {

    final private String TAG = "MEMORY-ACC";

    // Database Name
    private static final String sDatabaseName = "ItemDB";

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Items table name
    public static final String TABLE_ITEMS = "items";
    // Groups table name
    public static final String TABLE_GROUPS = "Groups";

    // Items Table Columns names
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_GROUP = "groupId";
    public static final String KEY_ITEMGROUP_UUID = "groupUUID";
    public static final String KEY_ITEM_UUID = "itemUUID";
    public static final String KEY_TITLE = "title";
    public static final String KEY_FRONT = "front";
    public static final String KEY_BACK = "back";
    public static final String KEY_BOOKMARK = "bookMark";
    public static final String KEY_STAMP = "stamp";
    public static final String KEY_PROTECTION = "protection";
    public static final String KEY_FEATURE_INT_0 = "featureInteger0";
    public static final String KEY_FEATURE_INT_1 = "featureInteger1";
    public static final String KEY_DATECREATION = "dateCreation";
    public static final String KEY_DATEUPDATE = "dateUpdate";
    public static final String KEY_FEATURE_TEXT_0 = "featureText0";
    public static final String KEY_FEATURE_TEXT_1 = "featureText1";


    // Groups Table Columns names
    public static final String KEY_GROUP_ROW_ID = "_id";
    public static final String KEY_GROUP_UUID = "uuid";
    public static final String KEY_GROUP_NAME = "groupName";

    private static final String[] COLUMNS_OF_TABLE_ITEMS = {
            KEY_ROW_ID,
            KEY_GROUP,
            KEY_ITEMGROUP_UUID,
            KEY_ITEM_UUID,
            KEY_TITLE,
            KEY_FRONT,
            KEY_BACK,
            KEY_BOOKMARK,
            KEY_STAMP,
            KEY_PROTECTION,
            KEY_FEATURE_INT_0,
            KEY_FEATURE_INT_1,
            KEY_DATECREATION,
            KEY_DATEUPDATE,
            KEY_FEATURE_TEXT_0,
            KEY_FEATURE_TEXT_1
    };

    private static final String[] COLUMNS_OF_TABLE_GROUPS = {
            KEY_GROUP_ROW_ID,
            KEY_GROUP_UUID,
            KEY_GROUP_NAME};

    private static DatabaseHelper sInstance;

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, sDatabaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create item table
        String CREATE_ITEM_TABLE = "create table " + TABLE_ITEMS + " ( "
                + KEY_ROW_ID + " integer primary key autoincrement , "
                + KEY_GROUP + " text  , "
                + KEY_ITEMGROUP_UUID + " text  , "
                + KEY_ITEM_UUID + " text  , "
                + KEY_TITLE + "  text  , "
                + KEY_FRONT + "  text  , "
                + KEY_BACK + "  text  , "
                + KEY_BOOKMARK + "  integer  , "
                + KEY_STAMP + "  integer  ,"
                + KEY_PROTECTION + "  integer  ,"
                + KEY_FEATURE_INT_0 + "  integer  ,"
                + KEY_FEATURE_INT_1 + "  integer  ,"
                + KEY_DATECREATION + "  text  ,"
                + KEY_DATEUPDATE + "  text  ,"
                + KEY_FEATURE_TEXT_0 + "  text  ,"
                + KEY_FEATURE_TEXT_1 + "  text ) ";

        // create items table
        db.execSQL(CREATE_ITEM_TABLE);

        String CREATE_GROUP_TABLE = "create table " + TABLE_GROUPS + " ( "
                + KEY_GROUP_ROW_ID + " integer primary key autoincrement , "
                + KEY_GROUP_UUID + " text , "
                + KEY_GROUP_NAME + " text  ) ";

        // create groups table
        db.execSQL(CREATE_GROUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 1 && newVersion == 2) {

            String addColumnProtection =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_PROTECTION + " integer DEFAULT 0";
            String addColumnFeatureInt0 =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_FEATURE_INT_0 + " integer DEFAULT 0";
            String addColumnFeatureInt1 =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_FEATURE_INT_1 + " integer DEFAULT 0";
            String addColumnDateCreation =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_DATECREATION + " text NOT NULL DEFAULT ''";
            String addColumnDateUpdate =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_DATEUPDATE + " text NOT NULL DEFAULT ''";
            String addColumnFeatureText0 =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_FEATURE_TEXT_0 + " text NOT NULL DEFAULT ''";
            String addColumnFeatureText1 =
                    "ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_FEATURE_TEXT_1 + " text NOT NULL DEFAULT ''";

            db.execSQL(addColumnProtection);
            db.execSQL(addColumnFeatureInt0);
            db.execSQL(addColumnFeatureInt1);
            db.execSQL(addColumnDateCreation);
            db.execSQL(addColumnDateUpdate);
            db.execSQL(addColumnFeatureText0);
            db.execSQL(addColumnFeatureText1);

            Log.d(TAG, "Database is upgraded from " +
                    String.valueOf(oldVersion) +
                    " to " +
                    String.valueOf(newVersion));
        }
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations on Item Table
     */
    // Add a new Item
    public void addItem(Group group, Item item) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP, group.getGroupName()); // get groupId
        values.put(KEY_ITEMGROUP_UUID, group.getUuid()); // get groupUUID
        values.put(KEY_ITEM_UUID, item.getUuid()); // get itemUUID
        values.put(KEY_TITLE, item.getTitle()); // get title
        values.put(KEY_FRONT, item.getFront()); // get front
        values.put(KEY_BACK, item.getBack()); // get back
        values.put(KEY_BOOKMARK, item.getBookmark()); // get bookMark
        values.put(KEY_STAMP, item.getStamp()); // get Stamp
        values.put(KEY_PROTECTION, item.getLock()); // get Lock
        values.put(KEY_FEATURE_INT_0, 0); // get feature_int_0
        values.put(KEY_FEATURE_INT_1, 0); // get feature_int_1
        values.put(KEY_DATECREATION, item.getDateCreation()); // get date creation
        values.put(KEY_DATEUPDATE, item.getDateUpdate()); // get date update
        values.put(KEY_FEATURE_TEXT_0, ""); // get feature_text_0
        values.put(KEY_FEATURE_TEXT_1, ""); // get feature_text_1

        // 3. insert
        db.insert(TABLE_ITEMS,
                null,
                values);

        // 4. close
        db.close();
    }

    // Get All Items
    public ArrayList<Item> getItemList(String groupUUID) {
        ArrayList<Item> items = new ArrayList<Item>();

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor =
                db.query(TABLE_ITEMS, // a. table
                        COLUMNS_OF_TABLE_ITEMS, // b. column names
                        KEY_ITEMGROUP_UUID + " = ?", // c. selections
                        new String[]{groupUUID}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. go over each row, build item and add it to list
        Item item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setUuid(cursor.getString(3));
                item.setTitle(cursor.getString(4));
                item.setFront(cursor.getString(5));
                item.setBack(cursor.getString(6));
                item.setBookmark(cursor.getInt(7));
                item.setStamp(cursor.getInt(8));
                item.setLock(cursor.getInt(9));
                //item.setFeatureInt0(cursor.getInt(10));
                //item.setFeatureInt1(cursor.getInt(11));
                item.setDateCreation(cursor.getString(12));
                item.setDateUpdate(cursor.getString(13));
                //item.setFeatureText0(cursor.getString(14));
                //item.setFeatureText1(cursor.getString(15));

                // Add item to items
                items.add(item);

            } while (cursor.moveToNext());
        }

        // return items
        Collections.reverse(items);
        return items;
    }

    // Updating a single item
    public int updateItem(Group group, Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP, group.getGroupName()); // get groupName
        values.put(KEY_ITEMGROUP_UUID, group.getUuid()); // get groupUUID
        values.put(KEY_ITEM_UUID, item.getUuid()); // get itemUUID
        values.put(KEY_TITLE, item.getTitle()); // get title
        values.put(KEY_FRONT, item.getFront()); // get front
        values.put(KEY_BACK, item.getBack()); // get back
        values.put(KEY_BOOKMARK, item.getBookmark()); // get bookMark
        values.put(KEY_STAMP, item.getStamp()); // get Stamp
        values.put(KEY_PROTECTION, item.getLock()); // get Lock
        values.put(KEY_DATECREATION, item.getDateCreation()); // get Date Creation
        values.put(KEY_DATEUPDATE, item.getDateUpdate()); // get Date Update

        // 3. updating row
        int i = db.update(TABLE_ITEMS, //table
                values, // column/value
                KEY_ITEM_UUID + " = ?", // selections
                new String[]{item.getUuid()}); //selection args

        // 4. close
        db.close();

        return i;
    }

    // Deleting a single item
    public void deleteItem(Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ITEMS,
                KEY_ITEM_UUID + " = ?",
                new String[]{item.getUuid()});

        // 3. close
        db.close();
    }

    // Deleting all items in a specific group
    public void deleteGroupItems(String uuid) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ITEMS,
                KEY_ITEMGROUP_UUID + " = ?",
                new String[]{uuid});

        // 3. close
        db.close();
    }

    /**
     * CRUD operations on Group Table
     */
    public void addGroup(Group group) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_UUID, group.getUuid()); // get groupName
        values.put(KEY_GROUP_NAME, group.getGroupName()); // get groupName

        // 3. insert
        db.insert(TABLE_GROUPS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Group getGroup(int groupId) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_GROUPS, // a. table
                        COLUMNS_OF_TABLE_GROUPS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(groupId)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build group object
        Group group = new Group();
        group.setGroupId(Integer.parseInt(cursor.getString(0)));
        group.setGroupName(cursor.getString(1));

        // 5. return group
        return group;
    }

    // Get All Groups
    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groups = new ArrayList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_GROUPS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build group and add it to list
        Group group = null;
        if (cursor.moveToFirst()) {
            do {
                group = new Group();
                group.setGroupId(Integer.parseInt(cursor.getString(0)));
                group.setUuid(cursor.getString(1));
                group.setGroupName(cursor.getString(2));

                // Add group to groups
                groups.add(group);

            } while (cursor.moveToNext());
        }

        // return groups
        return groups;
    }

    // Updating a single group
    public int updateGroup(Group group) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("groupName", group.getGroupName()); // get title


        // 3. updating row
        int i = db.update(TABLE_GROUPS, //table
                values, // column/value
                KEY_GROUP_UUID + " = ?", // selections
                new String[]{String.valueOf(group.getUuid())}); //selection args

        // 4. close
        db.close();

        return i;
    }

    // Deleting a single group
    public void deleteGroup(String uuid) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_GROUPS,
                KEY_GROUP_UUID + " = ?",
                new String[]{uuid});

        // 3. close
        db.close();
    }
}

