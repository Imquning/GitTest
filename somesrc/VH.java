package com.android.frame.adapter;

import android.util.SparseArray;
import android.view.View;

public class VH implements View.OnClickListener {

    private View view;
    private OnViewClick onViewClick;
    private SparseArray<View> cache;

    @Deprecated
    public VH(View view) {
        this.view = view;
        cache = new SparseArray<>();
    }

    public VH(View view, OnViewClick onViewClick) {
        this.view = view;
        this.onViewClick = onViewClick;
        cache = new SparseArray<>();
    }

    public <T extends View> T get(int id) {
        View v = cache.get(id);
        if (v == null) {
            v = view.findViewById(id);
            cache.put(id, v);
        }
        return (T) v;
    }

    public void addOnClickListener(int id) {
        View v = get(id);
        if (v == null) {
            return;
        }
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onViewClick != null) onViewClick.onViewClick(v);
    }

    public interface OnViewClick {
        void onViewClick(View view);
    }
}