package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cosmoqiu on 2/8/15.
 */
public class CardColor {

    static public List<CardColor> CardList = Arrays.asList(
            new CardColor("CHUNLYV", Color.rgb(227, 239, 209)),
            new CardColor("DANHUILYV", Color.rgb(174, 196, 183)),
            new CardColor("DANDONGSHI", Color.rgb(215, 193, 107)),
            new CardColor("GANSHIFEN", Color.rgb(234, 220, 214)),
            new CardColor("HUANGHUI", Color.rgb(176, 183, 172)),
            new CardColor("HUIMI", Color.rgb(182, 177, 150)),
            new CardColor("JUNLYV", Color.rgb(202, 212, 186)),
            new CardColor("LUHUI", Color.rgb(169, 176, 143)),
            new CardColor("MIHONG", Color.rgb(225, 189, 162)),
            new CardColor("MISE", Color.rgb(245, 245, 220)),
            new CardColor("NAILYV", Color.rgb(175, 200, 186)),
            new CardColor("QIANSHIYINGZI", Color.rgb(171, 150, 197)),
            new CardColor("QIANTENGZI", Color.rgb(196, 195, 203)),
            new CardColor("QIANXIEYA", Color.rgb(234, 205, 209)),
            new CardColor("SHUIHUANG", Color.rgb(190, 210, 182)),
            new CardColor("CHENSHA", Color.rgb(175, 94, 83)),
            new CardColor("BEIJINGMAOLAN", Color.rgb(39, 104, 147)),
            new CardColor("FEIHONG", Color.rgb(195, 86, 85)),
            new CardColor("JINHUANG", Color.rgb(199, 122, 58)),
            new CardColor("ZONGCHA", Color.rgb(184, 132, 79)),
            new CardColor("GANCAOHUANG", Color.rgb(228, 207, 142))
            );

    String _colorName;
    int _colorInt;

    public CardColor(String colorName, int colorInt) {
        _colorName = colorName;
        _colorInt = colorInt;
    }

    public String getColorName() {
        return _colorName;
    }

    public int getColorInt() {
        return _colorInt;
    }
}
