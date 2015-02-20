package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;

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
