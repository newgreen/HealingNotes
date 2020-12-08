package com.zhengyp.healingnotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class NavigationBar extends HorizontalScrollView {
    private final ArrayList<View> viewList = new ArrayList<>();
    private final LinearLayout linearLayout;
    private boolean needRefresh = true;

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public NavigationBar(Context context) {
        this(context, null);
    }

    public void addItem(View view) {
        viewList.add(view);
        refresh();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (needRefresh) {
            needRefresh = false;
            clearLayout();
            measureLayout();
        }
    }

    private void clearLayout() {
        for (View view : viewList) {
            linearLayout.removeView(view);
        }
    }

    private void measureLayout() {
        final int MAX_ITEM_TO_SHOW = 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getMeasuredWidth() / MAX_ITEM_TO_SHOW, getMeasuredHeight());

        for (View view : viewList) {
            linearLayout.addView(view, layoutParams);
        }
    }

    private void refresh() {
        needRefresh = true;
        requestLayout();
        invalidate();
    }
}