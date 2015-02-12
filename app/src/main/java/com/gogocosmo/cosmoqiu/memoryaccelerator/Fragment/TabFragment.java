package com.gogocosmo.cosmoqiu.memoryaccelerator.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty.CarouselActivity;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty.NewItemActivity;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    private ListView _listView;
    private Context _context;


    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.item_list,container,false);

        _listView = (ListView) v.findViewById(R.id.listview);

        final ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(_context,
                ItemFactory.getItemList());
        _listView.setAdapter(itemArrayAdapter);

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(_context, NewItemActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
