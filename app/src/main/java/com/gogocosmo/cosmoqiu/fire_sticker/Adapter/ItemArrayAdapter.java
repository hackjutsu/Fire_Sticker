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

    final private String TAG = "MEMORY-ACC";

    private Context mContext;
    private ArrayList<Item> mValues;
    private HashMap<Item, Long> mIdMap = new HashMap<>();

    public class ViewHolder {

        public TextView mFrontSide;
        public TextView mBackSide;
        public TextView mTitle;
        public ImageView mBookMark;
        public ImageView mStamp;
    }

    public ItemArrayAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.item_list_rowlayout_improved, values);
        this.mContext = context;
        this.mValues = values;

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
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(R.layout.item_list_rowlayout_improved, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.mFrontSide = (TextView) rowView.findViewById(R.id.item_question);
            viewHolder.mBackSide = (TextView) rowView.findViewById(R.id.item_answer);
            viewHolder.mTitle = (TextView) rowView.findViewById(R.id.item_title);
            viewHolder.mBookMark = (ImageView) rowView.findViewById(R.id.item_bookmark);
            viewHolder.mStamp = (ImageView) rowView.findViewById(R.id.item_done);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String frontStr = mValues.get(position).getFront();
        String backStr = mValues.get(position).getBack();
        int bookMark = mValues.get(position).getBookmark();
        int stamp = mValues.get(position).getStamp();

        if (frontStr.isEmpty()) {
            frontStr = "Blank Front Side";
        }

        if (backStr.isEmpty()) {
            backStr = "Blank Back Side";
        }

        if (bookMark == 1) {
            viewHolder.mBookMark.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mBookMark.setVisibility(View.INVISIBLE);
        }

        if (stamp == 1) {
            viewHolder.mStamp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mStamp.setVisibility(View.INVISIBLE);
        }

        // Replace multiple spaces to single space while displaying in item cell.
        viewHolder.mFrontSide.setText(frontStr.trim().replaceAll(" +", " "));
        viewHolder.mBackSide.setText(backStr.trim().replaceAll(" +", " "));
        viewHolder.mTitle.setText(mValues.get(position).getTitle());

        return rowView;
    }

    @Override
    public long getItemId(int position) {

        // Special condition for ListView's EmptyView. In this case, mValues is empty and we cannot
        // get item by calling getItem(id), which will return null pointer
        if (position == 0 && mIdMap.size() != 0 && mValues.size() == 0) {
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



