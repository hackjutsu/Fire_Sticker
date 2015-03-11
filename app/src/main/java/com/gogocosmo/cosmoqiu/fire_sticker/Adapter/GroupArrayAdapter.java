package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.Group;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.ArrayList;

public class GroupArrayAdapter extends ArrayAdapter<Group> {

    final private String TAG = "MEMORY-ACC";

    private final Context mContext;
    private final ArrayList<Group> mGroups;

    public class ViewHolder {

        public TextView mGroupName;
        public TextView mGroupSize;
        public ProgressBar mProgressBar;
    }

    public GroupArrayAdapter(Context context, ArrayList<Group> values) {
        super(context, R.layout.group_list_rowlayout_improved, values);
        this.mContext = context;
        this.mGroups = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        //reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(R.layout.group_list_rowlayout_improved, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.mGroupName = (TextView) rowView.findViewById(R.id.group_name);
            viewHolder.mGroupSize = (TextView) rowView.findViewById(R.id.group_size);
            viewHolder.mProgressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.mGroupName.setText(mGroups.get(position).getGroupName());
        viewHolder.mGroupSize.setText(String.valueOf(ItemFactory.getItemList(position).size()));

        int percentage = (int)((float)ItemFactory.getItemList(position).size()/50 * 100);
        viewHolder.mProgressBar.setProgress(percentage);

        return rowView;
    }
}
