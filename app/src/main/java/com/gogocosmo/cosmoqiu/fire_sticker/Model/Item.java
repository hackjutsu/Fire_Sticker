package com.gogocosmo.cosmoqiu.fire_sticker.Model;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class Item {

    private int id;
    private String _frontSide = null;
    private String _backSide = null;
    private String _title = null;
    private int _bookMark = 0;

    public Item() {

    }

    public Item(String frontSide, String backSide, String title) {
        _frontSide = frontSide;
        _backSide = backSide;
        _title = title;
    }

    public Item(String frontSide, String backSide, String title, int bookMark) {
        _frontSide = frontSide;
        _backSide = backSide;
        _title = title;
        _bookMark = bookMark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrontSide(String frontSide) {
        this._frontSide = frontSide;
    }

    public void setBackSide(String backSide) {
        this._backSide = backSide;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public void setBookMark(int bookMark) {
        _bookMark = bookMark;
    }

    public String getFrontSide() {
        return _frontSide;
    }

    public String getBackSide() {
        return _backSide;
    }

    public String getTitle() {
        return _title;
    }

    public int getBookMark() {
        return _bookMark;
    }

    public String toString() {
        return "Title: " + _title + "," + "Front: " + _frontSide + ", " + "Back: " + _backSide;
    }
}
