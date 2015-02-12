package com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter;

import android.graphics.Color;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.Item;

/**
 * Created by cosmoqiu on 2/8/15.
 */
public class CardHolder {

    int _color;
    Item _item;

    public CardHolder(int color, Item item) {
        _color = color;
        _item = item;
    }

    public CardHolder(int color) {
        _color = color;
    }

    public int getColor() {
        return _color;
    }

    public Item getItem() {
        return _item;
    }
}
