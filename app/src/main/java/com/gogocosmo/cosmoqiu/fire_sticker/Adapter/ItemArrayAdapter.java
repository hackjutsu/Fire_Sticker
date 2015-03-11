package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context _context;
    private final ArrayList<Item> _values;
    final private String TAG = "MEMORY-ACC";

    HashMap<Item, Long> mIdMap = new HashMap<>();


    public class ViewHolder {
        public TextView _frontSide;
        public TextView _backSide;
        public TextView _title;
        public ImageView _bookMark;
        public ImageView _stamp;
    }

    public ItemArrayAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.item_list_rowlayout_improved, values);
        this._context = context;
        this._values = values;

        for (int i = 0; i < values.size(); ++i) {

            mIdMap.put(values.get(i), randomLongIdGenerator());
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
            viewHolder._bookMark = (ImageView) rowView.findViewById(R.id.item_bookmark);
            viewHolder._stamp = (ImageView) rowView.findViewById(R.id.item_done);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

//        if (position == ItemFactory.getSelectedItemIndex()) {
//            LaunchActivity._selectedView = rowView;
//        }

        String frontStr = _values.get(position).getFront();
        String backStr = _values.get(position).getBack();
        int bookMark = _values.get(position).getBookMark();
        int stamp = _values.get(position).getStamp();

        if (frontStr.isEmpty()) {
            frontStr = "Blank Front Side";
        }

        if (backStr.isEmpty()) {
            backStr = "Blank Back Side";
        }

        if (bookMark == 1) {
            viewHolder._bookMark.setVisibility(View.VISIBLE);
        } else {
            viewHolder._bookMark.setVisibility(View.INVISIBLE);
        }

        if (stamp == 1) {
            viewHolder._stamp.setVisibility(View.VISIBLE);
        } else {
            viewHolder._stamp.setVisibility(View.INVISIBLE);
        }

        // Replace multiple spaces to single space while displaying in item cell.
        viewHolder._frontSide.setText(frontStr.trim().replaceAll(" +", " "));
        viewHolder._backSide.setText(backStr.trim().replaceAll(" +", " "));
        viewHolder._title.setText(_values.get(position).getTitle());


        return rowView;
    }

    @Override
    public long getItemId(int position) {

        // Special condition for ListView's EmptyView. In this case, _values is empty and we cannot
        // get item by calling getItem(id), which will return null pointer
        if (position == 0 && mIdMap.size() != 0 && _values.size() == 0) {
            return 0;
        }

        Item item = getItem(position);

        if (!mIdMap.containsKey(item)) {

            mIdMap.put(item, randomLongIdGenerator());
        }

        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void remove(Item object) {

        mIdMap.remove(object);
        super.remove(object);
    }

    private long randomLongIdGenerator() {

        return new Random().nextLong();
    }
}



