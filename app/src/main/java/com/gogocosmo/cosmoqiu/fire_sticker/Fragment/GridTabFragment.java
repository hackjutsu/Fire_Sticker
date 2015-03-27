package com.gogocosmo.cosmoqiu.fire_sticker.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapterGrid;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;


public class GridTabFragment extends Fragment {

    final private String TAG = "MEMORY-ACC";

    private GridView mGridView;
    private Context mContext;
    private OnTabListItemClickListener mTabListItemClickListener;
    private ItemArrayAdapterGrid mItemArrayAdapterGrid;
    private int mGroupId;


    public interface OnTabListItemClickListener {

        void OnListItemLongClicked(ArrayAdapter adapter,
                                   GridView listView,
                                   View view,
                                   int groupId,
                                   int position);

        void OnListItemClicked(ArrayAdapter adapter,
                               GridView listView,
                               View view,
                               int groupId,
                               int position);
    }

    public GridTabFragment() {

    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mGroupId = args.getInt("GROUP");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof OnTabListItemClickListener) {
            mTabListItemClickListener = (OnTabListItemClickListener) activity;
        } else {
            throw new RuntimeException(
                    "Host Activity should implement " +
                            "GridTabFragment.OnListItemLongClickListener interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState!=null) {

            Bundle bundle = getArguments();
            mGroupId = bundle.getInt("GROUP");
        }

        View v = inflater.inflate(R.layout.item_grid, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridView1);

        mItemArrayAdapterGrid = new ItemArrayAdapterGrid(mContext,
                ItemFactory.getItemList(mGroupId), mGroupId);
        mGridView.setEmptyView(v.findViewById(R.id.emptyElement));
        mGridView.setAdapter(mItemArrayAdapterGrid);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mTabListItemClickListener.OnListItemLongClicked(mItemArrayAdapterGrid, mGridView, view, mGroupId, position);
                return true;
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mTabListItemClickListener.OnListItemClicked(mItemArrayAdapterGrid, mGridView, view, mGroupId, position);
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("GROUP", mGroupId);
        super.onSaveInstanceState(outState);
    }
}

