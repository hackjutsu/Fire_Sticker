package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Acitivty.LaunchActivity;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context _context;
    private final ArrayList<Item> _values;
    final private String TAG = "MEMORY-ACC";

    HashMap<Item, Integer> mIdMap = new HashMap<>();


    public class ViewHolder {
        public TextView _frontSide;
        public TextView _backSide;
        public TextView _title;
        public ImageView _light;
    }

    public ItemArrayAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.item_list_rowlayout_improved, values);
        this._context = context;
        this._values = values;

        for (int i = 0; i < values.size(); ++i) {
            mIdMap.put(values.get(i), i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        //reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(_context);
            rowView = inflater.inflate(R.layout.item_list_rowlayout_improved, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder._frontSide = (TextView) rowView.findViewById(R.id.item_question);
            viewHolder._backSide = (TextView) rowView.findViewById(R.id.item_answer);
            viewHolder._title = (TextView) rowView.findViewById(R.id.item_title);
            viewHolder._light = (ImageView) rowView.findViewById(R.id.item_light);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

//        if (position == ItemFactory.getSelectedItemIndex()) {
//            LaunchActivity._selectedView = rowView;
//        }

        String frontStr = _values.get(position).getFrontSide();
        String backStr = _values.get(position).getBackSide();
        boolean light = _values.get(position).getBookMark();

        if (frontStr.isEmpty()) {
            frontStr = "Blank Front Side";
        }

        if (backStr.isEmpty()) {
            backStr = "Blank Back Side";
        }

        if (light == true) {
            viewHolder._light.setVisibility(View.VISIBLE);
        } else {
            viewHolder._light.setVisibility(View.INVISIBLE);
        }

        viewHolder._frontSide.setText(frontStr);
        viewHolder._backSide.setText(backStr);
        viewHolder._title.setText(_values.get(position).getTitle());


        return rowView;
    }

    @Override
    public long getItemId(int position) {

        if (position < 0 || position >= mIdMap.size()) {
            return -1;
        }

        Item item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}



