package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.R;


public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.ViewHolder> {

    final static private String TAG = "MEMORY-ACC";
    final static public int TYPE_HEADER = 0;
    final static public int TYPE_ITEM = 1;
    final static public int TYPE_EDIT = 2;
    final static public int TYPE_ABOUTAPP = 3;

    private String mNavTitles[];
    private int mProfile;
    private IDrawerListItemClickListener mDrawerItemClickListener;

    public interface IDrawerListItemClickListener {

        public void onDrawerItemClicked(View v, int position, int viewType);
    }

    // http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public static interface IViewHolderClickListener {

            public void onClickedEvent(View v, int position);
        }

        private int mViewType;
        private int mViewHolderPosition;

        private TextView mTextField;
        private ImageView mProfileImage;
        private IViewHolderClickListener mViewHolderClickListener;

        public ViewHolder(View itemView, int ViewType, IViewHolderClickListener listener) {
            super(itemView);

            mViewType = ViewType;
            mViewHolderClickListener = listener;

            if (ViewType == TYPE_ITEM) {

                mTextField = (TextView) itemView.findViewById(R.id.rowText);
                itemView.setOnClickListener(this);
            } else if (ViewType == TYPE_HEADER) {

                mProfileImage = (ImageView) itemView.findViewById(R.id.circleView);
                // Header doesn't need a ClickedListener
            } else if (ViewType == TYPE_EDIT) {

                mTextField = (TextView) itemView.findViewById(R.id.rowText);
                itemView.setOnClickListener(this);
            } else if (ViewType == TYPE_ABOUTAPP) {

                mTextField = (TextView) itemView.findViewById(R.id.rowText);
                itemView.setOnClickListener(this);
            }
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mViewHolderClickListener != null) {

                v.startAnimation(new AlphaAnimation(1.0f, 0.5f));
                mViewHolderClickListener.onClickedEvent(v, mViewHolderPosition);
            }
        }
    }

    public DrawerRecyclerViewAdapter(
            String Titles[],
            int profile,
            IDrawerListItemClickListener drawerItemClickListener) {

        mNavTitles = Titles;
        mProfile = profile;
        mDrawerItemClickListener = drawerItemClickListener;
    }

    @Override
    public DrawerRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.drawer_recyclerview_rowlayout, parent, false);

            ViewHolder vhItem = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

                    mDrawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_ITEM);
                }
            });
            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.drawer_recyclerview_header, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

//                    mDrawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_HEADER);
                }
            });
            return vhHeader;

        } else if (viewType == TYPE_EDIT) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.drawer_recyclerview_edit, parent, false);

            ViewHolder vhEnd = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

                    mDrawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_EDIT);
                }
            });
            return vhEnd;
        } else if (viewType == TYPE_ABOUTAPP) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.drawer_recyclerview_about, parent, false);

            ViewHolder vhEnd = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

                    mDrawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_ABOUTAPP);
                }
            });
            return vhEnd;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(DrawerRecyclerViewAdapter.ViewHolder holder, int position) {

        if (holder.mViewType == TYPE_ITEM) {

            holder.mTextField.setText(mNavTitles[position - 1]);
            holder.mViewHolderPosition = position;

        } else if (holder.mViewType == TYPE_HEADER) {

            holder.mProfileImage.setImageResource(mProfile);
            holder.mViewHolderPosition = position;
            //holder._email.setText(_email);
        } else if (holder.mViewType == TYPE_EDIT) {

            holder.mTextField.setText("Edit Group List");
            holder.mViewHolderPosition = position;
        } else if (holder.mViewType == TYPE_ABOUTAPP) {

            holder.mTextField.setText("About App");
            holder.mViewHolderPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        // Including the header and the edit section
        return mNavTitles.length + 3;
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionEditSection(position)) {
            return TYPE_EDIT;
        } else if (isPositionAboutSection(position)) {
            return TYPE_ABOUTAPP;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionEditSection(int position) {
        return position == getItemCount() - 2;
    }

    private boolean isPositionAboutSection(int position) {
        return position == getItemCount() - 1;
    }
}
