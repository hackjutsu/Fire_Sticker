package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import java.util.UUID;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class Item {

    private int id;
    private String _uuid;
    private String _front = null;
    private String _back = null;
    private String _title = null;
    private int _bookMark = 0;
    private int _stamp = 0;

    public Item() {

    }

    public Item(String front, String back, String title) {
        _front = front;
        _back = back;
        _title = title;
        _uuid = UUID.randomUUID().toString();
    }

    public Item(String front, String back, String title, int bookMark, int stamp) {
        _front = front;
        _back = back;
        _title = title;
        _bookMark = bookMark;
        _stamp = stamp;
        _uuid = UUID.randomUUID().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return _uuid;
    }

    public void setUuid(String uuid) {
        _uuid = uuid;
    }

    public void setFront(String front) {
        this._front = front;
    }

    public void setBack(String back) {
        this._back = back;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public void setBookMark(int bookMark) {
        _bookMark = bookMark;
    }

    public void setStamp(int _stamp) {
        this._stamp = _stamp;
    }

    public String getFront() {
        return _front;
    }

    public String getBack() {
        return _back;
    }

    public String getTitle() {
        return _title;
    }

    public int getBookMark() {
        return _bookMark;
    }

    public int getStamp() {
        return _stamp;
    }

    public String toString() {
        return "Title: " + _title + "," +
                "Front: " + _front + ", " +
                "Back: " + _back;
    }
}
