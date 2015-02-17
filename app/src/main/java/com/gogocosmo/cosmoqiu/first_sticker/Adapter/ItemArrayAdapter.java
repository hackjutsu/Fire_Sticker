package com.gogocosmo.cosmoqiu.first_sticker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.first_sticker.Acitivty.LaunchActivity;
import com.gogocosmo.cosmoqiu.first_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.first_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.first_sticker.R;

import java.util.ArrayList;


public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context _context;
    private final ArrayList<Item> _values;

    public class ViewHolder {
        public TextView _question;
        public TextView _answer;
    }

    public ItemArrayAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.item_list_rowlayout, values);
        this._context = context;
        this._values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        //reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(_context);
            rowView = inflater.inflate(R.layout.item_list_rowlayout, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder._question = (TextView) rowView.findViewById(R.id.label_question);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (position == ItemFactory.getSelectedItemIndex()) {
            rowView.setBackgroundColor(Color.parseColor("#E57373"));
            LaunchActivity._selectedView = rowView;
        } else {
            rowView.setBackgroundColor(Color.WHITE);
        }

        viewHolder._question.setText(_values.get(position).getQuestion());

        return rowView;
    }
}
