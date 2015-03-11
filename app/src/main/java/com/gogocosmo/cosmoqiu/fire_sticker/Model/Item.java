package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import java.util.UUID;

public class Item {

    private int mId;
    private String mUuid;
    private String mFront = null;
    private String mBack = null;
    private String mTitle = null;
    private int mBookmark = 0;
    private int mStamp = 0;

    public Item() {

    }

    public Item(String front, String back, String title) {
        mFront = front;
        mBack = back;
        mTitle = title;
        mUuid = UUID.randomUUID().toString();
    }

    public Item(String front, String back, String title, int bookMark, int stamp) {
        mFront = front;
        mBack = back;
        mTitle = title;
        mBookmark = bookMark;
        mStamp = stamp;
        mUuid = UUID.randomUUID().toString();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

    public void setFront(String front) {
        this.mFront = front;
    }

    public void setBack(String back) {
        this.mBack = back;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setBookMark(int bookMark) {
        mBookmark = bookMark;
    }

    public void setStamp(int stamp) {
        this.mStamp = stamp;
    }

    public String getFront() {
        return mFront;
    }

    public String getBack() {
        return mBack;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getBookMark() {
        return mBookmark;
    }

    public int getStamp() {
        return mStamp;
    }

    public String toString() {
        return "Title: " + mTitle + ", " +
                "Front: " + mFront + ", " +
                "Back: " + mBack + ", " +
                "Bookmark: " + String.valueOf(mBookmark) + ", " +
                "Stamp: " + String.valueOf(mStamp);
    }
}
