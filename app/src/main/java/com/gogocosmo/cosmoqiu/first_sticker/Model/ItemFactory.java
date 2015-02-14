package com.gogocosmo.cosmoqiu.first_sticker.Model;

import java.util.ArrayList;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class ItemFactory {

    private static ArrayList<Item> _itemList = new ArrayList<>();
    private static int _selectedItemIndex = -1;

    public static Item createItem(String question, String answer) {
        Item newItem = new Item(question, answer);
        _itemList.add(newItem);
        return newItem;
    }

    public static ArrayList<Item> getItemList() {
        return _itemList;
    }

    public static void setSelectedItemIndex(int selectedItemIndex) {
        _selectedItemIndex = selectedItemIndex;
    }

    public static int getSelectedItemIndex() {
        return _selectedItemIndex;
    }
}
