package com.gogocosmo.cosmoqiu.fire_sticker.Model;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class Item {

    private String _frontSide = null;
    private String _backSide = null;
    private String _title = null;
    private boolean _bookMark = false;

    public Item(String frontSide, String backSide, String title) {
        _frontSide = frontSide;
        _backSide = backSide;
        _title = title;
    }

    public Item(String frontSide, String backSide, String title, boolean bookMark) {
        _frontSide = frontSide;
        _backSide = backSide;
        _title = title;
        _bookMark = bookMark;
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

    public void setBookMark(boolean bookMark) {
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

    public boolean getBookMark() {
        return _bookMark;
    }

    public String toString() {
        return "Title: " + _title + "," + "Front: " + _frontSide + ", " + "Back: " + _backSide;
    }
}
