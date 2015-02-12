package com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.Item;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;

import java.util.ArrayList;

/**
 * Created by cosmoqiu on 2/3/15.
 */

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final ArrayList<Item> values;

    class ViewHolder {
        public TextView question;
        public TextView answer;
    }

    public ItemArrayAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        //reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.rowlayout, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.question = (TextView) rowView.findViewById(R.id.label_question);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.question.setText(values.get(position).getQuestion());

        return rowView;
    }
}
