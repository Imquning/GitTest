package com.android.frame.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.android.frame.adapter.RecyclerViewAdapter;

/**
 * Created by Imquning on 2017/8/30.
 */

public class NRecyclerView extends RecyclerView {
    public NRecyclerView(Context context) {
        super(context, null);
    }

    public NRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public NRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE && !canScrollVertically(1) && ra != null) ra.onBottom();
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    private RecyclerViewAdapter ra;

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof RecyclerViewAdapter) ra = (RecyclerViewAdapter) adapter;
    }
}
