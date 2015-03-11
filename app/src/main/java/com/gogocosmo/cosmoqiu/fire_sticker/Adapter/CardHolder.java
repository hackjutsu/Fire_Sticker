package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;

public class CardHolder {

    private int mColor;
    private Item mItem;

    public CardHolder(int color, Item item) {
        mColor = color;
        mItem = item;
    }

    public CardHolder(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public Item getItem() {
        return mItem;
    }
}
