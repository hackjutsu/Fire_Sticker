package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Acitivty.LaunchActivity;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.ArrayList;


public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private final Context _context;
    private final ArrayList<Item> _values;
    final private String TAG = "MEMORY-ACC";


    public class ViewHolder {
        public TextView _question;
        public TextView _answer;
        public TextView _title;
    }

    public ItemArrayAdapter(Context context, ArrayList<Item> values) {
        super(context, R.layout.improved_item_list_rowlayout, values);
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
            rowView = inflater.inflate(R.layout.improved_item_list_rowlayout, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder._question = (TextView) rowView.findViewById(R.id.item_question);
            viewHolder._answer = (TextView) rowView.findViewById(R.id.item_answer);
            viewHolder._title = (TextView) rowView.findViewById(R.id.item_title);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (position == ItemFactory.getSelectedItemIndex()) {
            LaunchActivity._selectedView = rowView;
        }

        viewHolder._question.setText(_values.get(position).getQuestion());
        viewHolder._answer.setText(_values.get(position).getAnswer());
        viewHolder._title.setText(_values.get(position).getTitle());

        return rowView;
    }
}
