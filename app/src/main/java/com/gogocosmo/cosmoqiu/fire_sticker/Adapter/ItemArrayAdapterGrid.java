package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class ItemArrayAdapterGrid extends ArrayAdapter<Item> {

    final private String TAG = "MEMORY-ACC";

    private Context mContext;
    private ArrayList<Item> mValues;
    private HashMap<Item, Long> mIdMap = new HashMap<>();
    private int mGroupId;

    public class ViewHolder {

        public TextView mFrontSide;
        public TextView mBackSide;
        public TextView mTitle;
        public TextView mUpdatedDate;
        public ImageView mBookMark;
        public ImageView mStamp;
        public ImageView mLock;
        public ImageView mPokerTop;
        public ImageView mPokerBottom;
    }

    public ItemArrayAdapterGrid(Context context, ArrayList<Item> values, int GroupId) {
        super(context, R.layout.item_list_rowlayout_improved, values);
        mContext = context;
        mValues = values;
        mGroupId = GroupId;

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
            rowView = inflater.inflate(R.layout.item_gridlayout, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.mFrontSide = (TextView) rowView.findViewById(R.id.item_question);
            viewHolder.mBackSide = (TextView) rowView.findViewById(R.id.item_answer);
            viewHolder.mTitle = (TextView) rowView.findViewById(R.id.item_title);
            viewHolder.mUpdatedDate = (TextView) rowView.findViewById(R.id.item_update_date);
            viewHolder.mBookMark = (ImageView) rowView.findViewById(R.id.item_bookmark);
            viewHolder.mStamp = (ImageView) rowView.findViewById(R.id.item_done);
            viewHolder.mLock = (ImageView) rowView.findViewById(R.id.item_lock);
            viewHolder.mPokerTop = (ImageView) rowView.findViewById(R.id.item_poker_top);
            viewHolder.mPokerBottom = (ImageView) rowView.findViewById(R.id.item_poker_bottom);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.mTitle.setText(mValues.get(position).getTitle());
        viewHolder.mUpdatedDate.setText("Edited " + mValues.get(position).getDateUpdate());

        String frontStr = mValues.get(position).getFront();
        String backStr = mValues.get(position).getBack();
        int bookMark = mValues.get(position).getBookmark();
        int stamp = mValues.get(position).getStamp();

        if (frontStr.isEmpty()) {
            frontStr = "";
        }

        if (backStr.isEmpty()) {
            backStr = "";
        }

        if (bookMark == 1) {
            viewHolder.mBookMark.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mBookMark.setVisibility(View.INVISIBLE);
        }

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean pokerPattern = preference.getBoolean("POKER", true);

        if (pokerPattern) {

            viewHolder.mPokerTop.setVisibility(View.VISIBLE);
            viewHolder.mPokerBottom.setVisibility(View.VISIBLE);

            int pokerType = position % 4;
            int pokerResourceTop;
            int pokerResourceBottom;
            switch (pokerType) {
                case 0:
                    pokerResourceTop = R.drawable.black_spade_k_top;
                    pokerResourceBottom = R.drawable.black_spade_k_upsidedown;
                    break;
                case 1:
                    pokerResourceTop = R.drawable.red_heart_a_top;
                    pokerResourceBottom = R.drawable.red_heart_a_upsidedown;
                    break;
                case 2:
                    pokerResourceTop = R.drawable.red_diamond_3_top;
                    pokerResourceBottom = R.drawable.red_diamond_3_upsidedown;
                    break;
                case 3:
                    pokerResourceTop = R.drawable.black_club_six_top;
                    pokerResourceBottom = R.drawable.black_club_six_upsidedown;
                    break;
                default:
                    pokerResourceTop = R.drawable.black_spade_k_top;
                    pokerResourceBottom = R.drawable.black_spade_k_upsidedown;
            }
            viewHolder.mPokerTop.setImageResource(pokerResourceTop);
            viewHolder.mPokerBottom.setImageResource(pokerResourceBottom);
        } else {

            viewHolder.mPokerTop.setVisibility(View.INVISIBLE);
            viewHolder.mPokerBottom.setVisibility(View.INVISIBLE);
        }

        if (mValues.get(position).getLock() == 1) {

            // If the note is locked, return rowView with lock stamp
            viewHolder.mFrontSide.setText("");
            viewHolder.mBackSide.setText(mValues.get(position).getFront());
            viewHolder.mStamp.setVisibility(View.INVISIBLE);
            viewHolder.mLock.setVisibility(View.VISIBLE);

            return rowView;
        } else {

            viewHolder.mLock.setVisibility(View.INVISIBLE);
        }

        if (stamp == 1) {
            viewHolder.mStamp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mStamp.setVisibility(View.INVISIBLE);
        }

        // Replace multiple spaces to single space while displaying in item cell.
        viewHolder.mFrontSide.setText(frontStr.trim().replaceAll(" +", " "));
        viewHolder.mBackSide.setText(backStr.trim().replaceAll(" +", " "));

        return rowView;
    }

    @Override
    public long getItemId(int position) {

        // Special condition for ListView's EmptyView. In this case, mValues is empty and we cannot
        // get item by calling getItem(id), which will return null pointer
        if (position == 0 && mValues.size() == 0) {
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



