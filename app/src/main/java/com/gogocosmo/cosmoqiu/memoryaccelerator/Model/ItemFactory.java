package com.gogocosmo.cosmoqiu.memoryaccelerator.Model;

import java.util.ArrayList;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class ItemFactory {

    private static ArrayList<Item> ItemList = new ArrayList<>();

    public static Item createItem(String question, String answer) {
        Item newItem = new Item(question, answer);
        ItemList.add(newItem);
        return newItem;
    }

    public static ArrayList<Item> getItemList() {
        return ItemList;
    }


}
