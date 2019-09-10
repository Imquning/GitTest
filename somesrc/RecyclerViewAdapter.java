package com.android.frame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Imquning on 2017/6/27.
 */

public class RecyclerViewAdapter<DATA> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFRESHING = 1;
    public static final int STATE_REFRESH_FAILED = 2;
    public static final int STATE_LOADING = 3;
    public static final int STATE_LOAD_ALL = 4;
    public static final int STATE_LOAD_FAILED = 5;

//    public static final int TYPE_VIEW_HEADER = -1;
//    public static final int TYPE_VIEW_BODY = 0;
//    public static final int TYPE_VIEW_FOOTER = 1;

//    public static final int TIP_LOAD_MORE_VIEW_NORMAL = 0;
//    public static final int TIP_REFRESH_VIEW_REFRESHING = 1;
//    public static final int TIP_REFRESH_VIEW_REFRESH_FAILED = 2;
//    public static final int TIP_LOAD_MORE_VIEW_LOADING = 3;
//    public static final int TIP_LOAD_MORE_VIEW_LOAD_ALL = 4;
//    public static final int TIP_LOAD_MORE_VIEW_LOAD_FAILED = 5;

    private Context context;
    private List<DATA> list;
    //    private List<Integer> layouts;
    private int layout;
    private RecyclerViewAdapterInterface recyclerViewAdapterInterface;
//    private OnItemClickListener<DATA> onItemClickListener;
//    private OnViewsInItemClickListener<DATA> onViewsInItemClickListener;

    private DataController<DATA> dataController;
    private int state = STATE_NORMAL;
    private boolean hasMore = true;

//    private RefreshDataReceiver<DATA> refreshDataReceiver;
//    private LoadMoreDataReceiver<DATA> loadMoreDataReceiver;

    private DataReceiver<DATA> dataReceiver;

    private View loadMoreView;
    private List<View> headers;
    private List<View> footers;

    public RecyclerViewAdapter(RecyclerViewAdapterInterface recyclerViewAdapterInterface, Context context, List<DATA> list, int layout, View loadMoreView) {
        this.recyclerViewAdapterInterface = recyclerViewAdapterInterface;
        this.context = context;
        this.list = new ArrayList<>();
        if (list != null) this.list.addAll(list);
//        this.layouts = new ArrayList<>();
//        if (layouts != null) this.layouts.addAll(layouts);
        this.layout = layout;
        this.loadMoreView = loadMoreView;
        headers = new ArrayList<>();
        footers = new ArrayList<>();
    }

    public RecyclerViewAdapter(RecyclerViewAdapterInterface recyclerViewAdapterInterface, Context context, List<DATA> list, View loadMoreView) {
        this.recyclerViewAdapterInterface = recyclerViewAdapterInterface;
        this.context = context;
        this.list = new ArrayList<>();
        if (list != null) this.list.addAll(list);
//        this.layouts = new ArrayList<>();
//        if (layouts != null) this.layouts.addAll(layouts);
//        this.layout = layout;
        this.loadMoreView = loadMoreView;
        headers = new ArrayList<>();
        footers = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e("onCreateViewHolder", "" + viewType);
        View view = null;
        if (viewType > 0) {
            view = LayoutInflater.from(context).inflate(viewType, parent, false);
        }
        if (viewType < -99) {
            view = footers.get(-100 - viewType);
        }
        if (viewType < 0) {
            view = headers.get(-viewType);
        }
        // Footer view
        if (viewType == 0) {
            view = loadMoreView;
        }
        ViewHolder viewHolder = new ViewHolder(view, -1);
        viewHolder.view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.position = position;
        holder.view.setTag(holder);
        if (position < headers.size() + list.size()) {
            DATA data = list.get(position - headers.size());
            recyclerViewAdapterInterface.onBindViewHolder(holder.vh, data, position - headers.size(), list, data instanceof Data ? ((Data) data).getLayout() : -1);
        }
    }

    @Override
    public int getItemCount() {
        return headers.size() + list.size() + footers.size() + (loadMoreView == null ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headers.size()) return -position;
        else if (position < headers.size() + list.size()) {
            DATA data = list.get(position - headers.size());
            if (data instanceof Data) return ((Data) data).getLayout();
//            else return layouts.get(0);
            else return layout;
        } else if (position < headers.size() + list.size() + footers.size()) {
            return -100 - position + headers.size() + list.size();
        } else return 0;// Load more view
    }

    @Override
    public void onClick(View v) {
        if (recyclerViewAdapterInterface != null) {
            RecyclerViewAdapter.ViewHolder holder = (ViewHolder) v.getTag();
            recyclerViewAdapterInterface.onItemClickListener(holder.position == -1 ? null : list.get(holder.position), holder.position, list, holder.vh, v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements VH.OnViewClick {
        private View view;
        private int position;
        private VH vh;

        public ViewHolder(View view, int position) {
            super(view);
            this.view = view;
            this.position = position;
            vh = new VH(view, this);
        }

        @Override
        public void onViewClick(View v) {
            if (recyclerViewAdapterInterface != null) {
                recyclerViewAdapterInterface.onViewsInItemClickListener(position == -1 ? null : list.get(position), position, list, vh, view, v);
            }
        }
    }

    public interface RecyclerViewAdapterInterface<DATA> {
        void onBindViewHolder(VH vh, DATA vo, int position, List<DATA> list, int layoutId);

        void onViewsInItemClickListener(DATA data, int position, List<DATA> list, VH vh, View item, View viewInItem);

        void onItemClickListener(DATA data, int position, List<DATA> list, VH vh, View view);

        void onStateChanged(View loadMore, int loadMoreState);
    }

//    public interface OnItemClickListener<DATA> {
//        void onItemClickListener(DATA data, int position, List<DATA> list, VH vh, View view);
//    }

//    public void setOnItemClickListener(OnItemClickListener<DATA> onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }

//    public interface OnViewsInItemClickListener<DATA> {
//        void onViewsInItemClickListener(DATA data, int position, List<DATA> list, VH vh, View item, View viewInItem);
//    }

//    public void setOnViewsInItemClickListener(OnViewsInItemClickListener<DATA> onViewsInItemClickListener) {
//        this.onViewsInItemClickListener = onViewsInItemClickListener;
//    }

    public int getDataCount() {
        return list.size();
    }

    public DATA getData(int position) {
        return list.get(position);
    }

    public void addItem(DATA data) {
        if (data == null) return;
        this.list.add(data);
        notifyDataSetChanged();
    }

    public void insertItem(int index, DATA data) {
        if (data != null) {
            this.list.add(index, data);
            notifyItemInserted(index);
        }
    }

    public void addItems(List<DATA> list) {
        if (list == null) return;
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void insertItems(int index, List<DATA> list) {
        if (list == null) return;
        this.list.addAll(index, list);
        notifyItemRangeInserted(index, list.size());
    }

    public void removeAllData() {
        list.clear();
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public int getDataPosition(DATA data) {
        if (data == null) return -1;
        return list.indexOf(data);
    }

    public int getDataPosition(OnDataCompareListener<DATA> listener) {
        if (listener == null) return -1;
        for (int i = 0; i < list.size(); i++) {
            if (listener.onDataCompare(list.get(i))) return i;
        }
        return -1;
    }

    public interface OnDataCompareListener<DATA> {
        boolean onDataCompare(DATA data);
    }

    public int updateData(DATA data) {
        int position = getDataPosition(data);
        if (position > -1) notifyItemChanged(position);
        return position;
    }

    public List<DATA> getDataCopy() {
        List<DATA> l = new ArrayList<>();
        l.addAll(list);
        return l;
    }

    public void addHeaderView(View view) {
        if (view == null) return;
        headers.add(view);
    }


    public void removeHeaderView(View view) {
        if (view == null) return;
        headers.remove(view);
    }

    public void addFooterView(View view) {
        if (view == null) return;
        footers.add(view);
    }

    public void removeFooterView(View view) {
        if (view == null) return;
        footers.remove(view);
    }

    public static class Data<DATA> {

        private DATA data;
        private int layout;

        public DATA getData() {
            return data;
        }

        public void setData(DATA data) {
            this.data = data;
        }

        public int getLayout() {
            return layout;
        }

        public void setLayout(int layout) {
            this.layout = layout;
        }
    }

    // --------- 下拉刷新、上拉加载更多 ---------

    public interface DataController<DATA> {
        void onRefresh(DataReceiver<DATA> refreshDataReceiver);

        void onLoadMore(DataReceiver<DATA> loadMoreDataReceiver);
    }

    public void refresh() {
        if (state == STATE_REFRESHING) return;
        state = STATE_REFRESHING;
        onStateChanged();
        if (dataController != null) dataController.onRefresh(dataReceiver);
    }

    public void loadMore() {
        onBottom();
    }

    public void onBottom() {
        if (state == STATE_REFRESHING || state == STATE_LOADING) return;
        if (hasMore) {
            if (dataController != null) {
                state = STATE_LOADING;
                dataController.onLoadMore(dataReceiver);
            }
        }
        onStateChanged();
    }

    public void setDataController(DataController<DATA> dataController) {
        this.dataController = dataController;
        if (dataReceiver == null) dataReceiver = new DataReceiver<DATA>() {
            @Override
            public void receiveRefreshData(List<DATA> list) {
                if (state != STATE_REFRESHING) return;
                if (list == null) {
                    refreshFailed();
                    noMore();
                } else refreshSuccess(list);
            }

            @Override
            public void receiveLoadMoreData(List<DATA> list) {
                if (state != STATE_LOADING) return;
                if (list == null) loadMoreFailed();
                else loadMoreSuccess(list);
            }

            @Override
            public void noMore() {
                hasMore = false;
                onStateChanged();
            }

            @Override
            public void hasMore() {
                if (state == STATE_REFRESH_FAILED) return;
                hasMore = true;
                onStateChanged();
            }
        };
//        if (loadMoreDataReceiver == null) loadMoreDataReceiver = new LoadMoreDataReceiver<DATA>() {
//            @Override
//            public void receiveLoadMoreData(List<DATA> list) {
//                if (state != STATE_LOADING) return;
//                if (list == null) loadMoreFailed();
//                else loadMoreSuccess(list);
//            }
//
//            @Override
//            public void noMore() {
//                hasMore = false;
//                onStateChanged();
//            }
//
//            @Override
//            public void hasMore() {
//                hasMore = true;
//                onStateChanged();
//            }
//        };
    }

    private void refreshSuccess(List<DATA> list) {
        state = STATE_NORMAL;
        this.list.clear();
        if (list.size() > 0) {
            this.list.addAll(list);
        }
//        handler.sendMessage(handler.obtainMessage(NOTIFY));
        notifyDataSetChanged();
        hasMore = true;
        onStateChanged();
    }

    private void refreshFailed() {
        state = STATE_REFRESH_FAILED;
        onStateChanged();
    }

    private void loadMoreSuccess(List<DATA> list) {
        state = STATE_NORMAL;
        if (list.size() > 0) {
            this.list.addAll(list);
//            handler.sendMessage(handler.obtainMessage(NOTIFY));
            notifyDataSetChanged();
        }
        onStateChanged();
    }

    private void loadMoreFailed() {
        state = STATE_LOAD_FAILED;
        onStateChanged();
    }

//    public void setLoadMoreTips(int index, String tip) {
//        loadMoreViewTips.put(index, tip);
//    }

    private void onStateChanged() {
        recyclerViewAdapterInterface.onStateChanged(loadMoreView, state == STATE_NORMAL && !hasMore ? STATE_LOAD_ALL : state);
//        if (loadMoreView == null) return;
//        RA_RL.ViewHolder holder = (ViewHolder) loadMoreView.getTag();
//        TextView tv = (TextView) holder.vh.get(loadMoreViewTipId);
//        switch (state) {
//            case STATE_NORMAL:
//                if (hasMore) tv.setText(loadMoreViewTips.get(TIP_LOAD_MORE_VIEW_NORMAL));
//                else tv.setText(loadMoreViewTips.get(TIP_LOAD_MORE_VIEW_LOAD_ALL));
//                break;
//            case STATE_REFRESHING:
//                tv.setText(loadMoreViewTips.get(TIP_REFRESH_VIEW_REFRESHING));
//                break;
//            case STATE_REFRESH_FAILED:
//                tv.setText(loadMoreViewTips.get(TIP_REFRESH_VIEW_REFRESH_FAILED));
//                break;
//            case STATE_LOADING:
//                tv.setText(loadMoreViewTips.get(TIP_LOAD_MORE_VIEW_LOADING));
//                break;
//            case STATE_LOAD_ALL:
//                tv.setText(loadMoreViewTips.get(TIP_LOAD_MORE_VIEW_LOAD_ALL));
//                break;
//            case STATE_LOAD_FAILED:
//                tv.setText(loadMoreViewTips.get(TIP_LOAD_MORE_VIEW_LOAD_FAILED));
//                break;
//        }
    }

//    public interface RefreshDataReceiver<DATA> extends DateReceiver {
//        void receiveRefreshData(List<DATA> list);
//    }

//    public interface LoadMoreDataReceiver<DATA> extends DateReceiver {
//        void receiveLoadMoreData(List<DATA> list);
//    }

    public interface DataReceiver<DATA> {
        void receiveRefreshData(List<DATA> list);

        void receiveLoadMoreData(List<DATA> list);

        void noMore();

        void hasMore();
    }

//    private static final int NOTIFY = 1 << 8;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == NOTIFY) notifyDataSetChanged();
//        }
//    };
}