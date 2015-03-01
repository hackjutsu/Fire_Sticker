package com.gogocosmo.cosmoqiu.fire_sticker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.Group;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemsTableHelper extends SQLiteOpenHelper {

    final private String TAG = "MEMORY-ACC";

    // Database Name
    private static final String DATABASE_NAME = "ItemDB";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Items table name
    public static final String TABLE_ITEMS = "items";
    // Groups table name
    public static final String TABLE_GROUPS = "Groups";

    // Items Table Columns names
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_GROUP = "groupId";
    public static final String KEY_ITEMGROUP_UUID = "groupUUID";
    public static final String KEY_TITLE = "title";
    public static final String KEY_FRONT = "front";
    public static final String KEY_BACK = "back";
    public static final String KEY_BOOKMARK = "bookMark";

    // Groups Table Columns names
    public static final String KEY_GROUP_ROW_ID = "_id";
    public static final String KEY_GROUP_UUID = "uuid";
    public static final String KEY_GROUP_NAME = "groupName";

    private static final String[] COLUMNS_OF_TABLE_ITEMS = {
            KEY_ROW_ID, KEY_GROUP, KEY_ITEMGROUP_UUID, KEY_TITLE, KEY_FRONT, KEY_BACK, KEY_BOOKMARK};

    private static ItemsTableHelper sInstance;

    public static ItemsTableHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ItemsTableHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public ItemsTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create item table
        String CREATE_ITEM_TABLE = "create table " + TABLE_ITEMS + " ( "
                + KEY_ROW_ID + " integer primary key autoincrement , "
                + KEY_GROUP + " text  , "
                + KEY_ITEMGROUP_UUID + " text  , "
                + KEY_TITLE + "  text  , "
                + KEY_FRONT + "  text  , "
                + KEY_BACK + "  text  , "
                + KEY_BOOKMARK + "  integer  ) ";

        // create items table
        db.execSQL(CREATE_ITEM_TABLE);

        Log.d(TAG, "CREATE_GROUP_TABLE");
        String CREATE_GROUP_TABLE = "create table " + TABLE_GROUPS + " ( "
                + KEY_GROUP_ROW_ID + " integer primary key autoincrement , "
                + KEY_GROUP_UUID + " text , "
                + KEY_GROUP_NAME + " text  ) ";

        // create groups table
        db.execSQL(CREATE_GROUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEMS);

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
    public void addItem(Group group, Item item) {
//        Log.d(TAG, "addItem: " + item.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP, group.getGroupName()); // get groupId
        values.put(KEY_ITEMGROUP_UUID, group.getUuid()); // get groupId
        values.put(KEY_TITLE, item.getTitle()); // get title
        values.put(KEY_FRONT, item.getFront()); // get front
        values.put(KEY_BACK, item.getBack()); // get back
        values.put(KEY_BOOKMARK, item.getBookMark()); // get back

        // 3. insert
        db.insert(TABLE_ITEMS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
//        db.close();
    }

    public Item getItem(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_ITEMS, // a. table
                        COLUMNS_OF_TABLE_ITEMS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build item object
        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setTitle(cursor.getString(2));
        item.setFront(cursor.getString(3));
        item.setBack(cursor.getString(4));
        item.setBookMark(cursor.getInt(5));

//        Log.d(TAG, "getItem(" + id + ")" + item.toString());

        // 5. return item
        return item;
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
//        Log.d(TAG, "getAllItems(): \n");
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(3));
                item.setFront(cursor.getString(4));
                item.setBack(cursor.getString(5));
                item.setBookMark(cursor.getInt(6));

                // Add item to items
                items.add(item);

//                Log.d(TAG, cursor.getString(1) + ": " + item.toString() + '\n');
            } while (cursor.moveToNext());
        }

        // return items
        return items;
    }

    public Cursor getAllItemsCursor() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM " + TABLE_ITEMS;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    // Updating single item
    public int updateItem(String groupId, Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("groupId", groupId); // get groupId
        values.put("title", item.getTitle()); // get title
        values.put("front", item.getFront()); // get front
        values.put("back", item.getBack()); // get back
        values.put("bookMark", item.getBookMark()); // get back

        // 3. updating row
        int i = db.update(TABLE_ITEMS, //table
                values, // column/value
                KEY_ROW_ID + " = ?", // selections
                new String[]{String.valueOf(item.getId())}); //selection args

        // 4. close
//        db.close();

        return i;
    }

    // Deleting single item
    public void deleteItem(int itemId) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ITEMS,
                KEY_ROW_ID + " = ?",
                new String[]{String.valueOf(itemId)});

        // 3. close
//        db.close();

        Log.d(TAG, "deleteItem: " + String.valueOf(itemId));
    }

    public void deleteGroupItems(String uuid) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ITEMS,
                KEY_ITEMGROUP_UUID + " = ?",
                new String[]{uuid});

        // 3. close
//        db.close();
    }

}

