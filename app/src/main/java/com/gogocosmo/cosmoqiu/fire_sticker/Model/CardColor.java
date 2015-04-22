package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;

public class CardColor {

    static public List<CardColor> COLOR_LIST = Arrays.asList(
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
            new CardColor("CHENSHA", Color.rgb(175, 94, 83)),
            new CardColor("BEIJINGMAOLAN", Color.rgb(39, 104, 147)),
            new CardColor("FEIHONG", Color.rgb(195, 86, 85)),
            new CardColor("ZONGCHA", Color.rgb(184, 132, 79)),
            new CardColor("QINGSE1", Color.rgb(102, 179, 149)),
            new CardColor("QINGSE2", Color.rgb(112, 197, 189)),
            new CardColor("SHIHUANG", Color.rgb(233, 202, 90)),
            new CardColor("QIANSHIHUANG", Color.rgb(228, 207, 142)),
            new CardColor("TAIKONGHUI", Color.rgb(243, 243, 243)), //1
            new CardColor("TAIKONGHUI", Color.rgb(243, 243, 243)), //2
            new CardColor("TAIKONGHUI", Color.rgb(243, 243, 243)), //3
            new CardColor("TAIKONGHUI", Color.rgb(243, 243, 243)), //4
            new CardColor("WANGQING", Color.rgb(85, 151, 161))
    );

    private String mColorName;
    private int mColorInt;

    public CardColor(String colorName, int colorInt) {
        mColorName = colorName;
        mColorInt = colorInt;
    }

    public String getColorName() {
        return mColorName;
    }

    public int getColorInt() {
        return mColorInt;
    }
}
