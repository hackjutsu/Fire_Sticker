package com.gogocosmo.cosmoqiu.first_sticker.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.first_sticker.R;


public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.ViewHolder> {

    final static private String TAG = "MEMORY-ACC";
    final static public int TYPE_HEADER = 0;
    final static public int TYPE_ITEM = 1;
    final static public int TYPE_END = 2;

    private String _navTitles[];
    private String _name;
    private int _profile;
    private String _email;

    private IDrawerListItemClickListener _drawerItemClickListener;


    public interface IDrawerListItemClickListener {

        public void onDrawerItemClicked(View v, int position, int viewType);
    }

    // http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public static interface IViewHolderClickListener {

            public void onClickedEvent(View v, int position);
        }

        private int _viewType;
        private int _position;

        private TextView _textView;
        private ImageView _imageView;
        private ImageView _profile;
        private TextView _name;
        private TextView _email;
        private IViewHolderClickListener _viewHolderClickListener;

        public ViewHolder(View itemView, int ViewType, IViewHolderClickListener listener) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {

                _textView = (TextView) itemView.findViewById(R.id.rowText);
                _imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
            } else if (ViewType == TYPE_HEADER) {

                _name = (TextView) itemView.findViewById(R.id.name);
                _email = (TextView) itemView.findViewById(R.id.email);
                _profile = (ImageView) itemView.findViewById(R.id.circleView);
            } else if (ViewType == TYPE_END) {

                _textView = (TextView) itemView.findViewById(R.id.rowText);
                _imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
            }

            _viewType = ViewType;
            _viewHolderClickListener = listener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if (_viewHolderClickListener != null) {

                v.startAnimation(new AlphaAnimation(1.0f, 0.5f));
                _viewHolderClickListener.onClickedEvent(v, _position);
            }
        }
    }

    public DrawerRecyclerViewAdapter(
            String Titles[],
            String name,
            String email,
            int profile,
            IDrawerListItemClickListener drawerItemClickListener) {

        _navTitles = Titles;
        _name = name;
        _email = email;
        _profile = profile;
        _drawerItemClickListener = drawerItemClickListener;
    }

    @Override
    public DrawerRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.drawer_recyclerview_rowlayout, parent, false);

            ViewHolder vhItem = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

                    _drawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_ITEM);
                }
            });
            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.header, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

                    _drawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_HEADER);
                }
            });
            return vhHeader;

        } else if (viewType == TYPE_END) {

            View v = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.drawer_recyclerview_rowlayout, parent, false);

            ViewHolder vhEnd = new ViewHolder(v, viewType, new ViewHolder.IViewHolderClickListener() {
                @Override
                public void onClickedEvent(View v, int position) {

                    _drawerItemClickListener.onDrawerItemClicked(v, position - 1, TYPE_END);
                }
            });
            return vhEnd;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(DrawerRecyclerViewAdapter.ViewHolder holder, int position) {

        if (holder._viewType == TYPE_ITEM) {

            holder._textView.setText(_navTitles[position - 1]);
            holder._position = position;

        } else if (holder._viewType == TYPE_HEADER) {

            holder._profile.setImageResource(_profile);
            holder._position = position;
            //holder.Name.setText(_name);
            //holder._email.setText(_email);
        } else if (holder._viewType == TYPE_END) {

            holder._textView.setText("NEW GROUP");
            holder._position = position;
        }
    }

    @Override
    public int getItemCount() {
        return _navTitles.length + 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionEndSection(position)) {
            return TYPE_END;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionEndSection(int position) {
        return position == getItemCount() - 1;
    }
}
