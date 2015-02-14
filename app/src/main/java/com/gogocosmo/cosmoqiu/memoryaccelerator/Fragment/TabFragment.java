package com.gogocosmo.cosmoqiu.memoryaccelerator.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty.NewItemActivity;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;


public class TabFragment extends Fragment {

    private ListView _listView;
    private Context _context;
    private OnListItemLongClickListener _longClickListener;
    private int _selectedItem = -1;

    public interface OnListItemLongClickListener {

        void OnListItemLongClicked(View view, int position);
    };

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _context = activity;
        if (activity instanceof OnListItemLongClickListener) {
            _longClickListener = (OnListItemLongClickListener) activity;
        } else {
            throw new RuntimeException(
                    "Host Activity should implement " +
                            "TabFragment.OnListItemLongClickListener interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_list, container, false);

        _listView = (ListView) v.findViewById(R.id.listview);

        final ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(_context,
                ItemFactory.getItemList());
        _listView.setAdapter(itemArrayAdapter);

//        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(_context, NewItemActivity.class);
//                startActivity(intent);
//            }
//        });

        _listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                _longClickListener.OnListItemLongClicked(view, position);
                return true;
            }
        });

        return v;
    }

    public void setSelectedItem(int selectedItem) {
        this._selectedItem = selectedItem;
    }
}
