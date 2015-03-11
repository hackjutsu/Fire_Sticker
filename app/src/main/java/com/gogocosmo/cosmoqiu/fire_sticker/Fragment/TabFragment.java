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

    final private String TAG = "MEMORY-ACC";

    private ListView mListView;
    private Context mContext;
    private OnTabListItemClickListener mTabListItemClickListener;
    private ItemArrayAdapter mItemArrayAdapter;
    private int mGroupId;


    public interface OnTabListItemClickListener {

        void OnListItemLongClicked(ItemArrayAdapter adapter,
                                   ListView listView,
                                   View view,
                                   int groupId,
                                   int position);

        void OnListItemClicked(ItemArrayAdapter adapter,
                               ListView listView,
                               View view,
                               int groupId,
                               int position);
    }

    public TabFragment() {

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
                            "TabFragment.OnListItemLongClickListener interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState!=null) {

            Bundle bundle = getArguments();
            mGroupId = bundle.getInt("GROUP");
        }

        View v = inflater.inflate(R.layout.item_list, container, false);

        mListView = (ListView) v.findViewById(R.id.listview);

        mItemArrayAdapter = new ItemArrayAdapter(mContext,
                ItemFactory.getItemList(mGroupId));
        mListView.setEmptyView(v.findViewById(R.id.emptyElement));
        mListView.setAdapter(mItemArrayAdapter);



        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mTabListItemClickListener.OnListItemLongClicked(mItemArrayAdapter, mListView, view, mGroupId, position);
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mTabListItemClickListener.OnListItemClicked(mItemArrayAdapter, mListView, view, mGroupId, position);
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
