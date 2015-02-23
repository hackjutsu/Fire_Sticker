package com.gogocosmo.cosmoqiu.fire_sticker.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;


public class TabFragment extends Fragment {

    private ListView _listView;
    private Context _context;
    private OnTabListItemClickListener _tabListItemClickListener;
    private ItemArrayAdapter _itemArrayAdapter;

    public interface OnTabListItemClickListener {

        void OnListItemLongClicked(ListView listView, View view, int position);

        void OnListItemClicked(ListView listView, View view, int position);
    }

    ;

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _context = activity;
        if (activity instanceof OnTabListItemClickListener) {
            _tabListItemClickListener = (OnTabListItemClickListener) activity;
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

        _itemArrayAdapter = new ItemArrayAdapter(_context,
                ItemFactory.getItemList());
        _listView.setAdapter(_itemArrayAdapter);

        _listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                _tabListItemClickListener.OnListItemLongClicked(_listView, view, position);
                return true;
            }
        });

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                _tabListItemClickListener.OnListItemClicked(_listView, view, position);
            }
        });
        return v;
    }

    public void notifyDataChanged() {
        if (_itemArrayAdapter != null) {
            _itemArrayAdapter.notifyDataSetChanged();
        }
    }
}
