package com.zhengyp.healingnotes;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainView {
    private final ConstraintLayout mainView;
    private final NavigationBar navigationBar;
    private final ConstraintLayout.LayoutParams contentLayoutParams;
    private NavigationBarItem focusItem = null;

    public MainView(@NonNull ConstraintLayout mainView) {
        this.mainView = mainView;
        navigationBar = mainView.findViewById(R.id.navigationBar);

        contentLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                mainView.getMeasuredHeight() - navigationBar.getMeasuredHeight());
        contentLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        contentLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        contentLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        contentLayoutParams.bottomToTop = R.id.navigationBar;
    }

    public int addItem(String title, int focusIcon, int unfocusedIcon, int layoutResId) {
        View newLayout = LayoutInflater.from(mainView.getContext()).inflate(layoutResId, null);
        return addItem(title, focusIcon, unfocusedIcon, newLayout);
    }

    public int addItem(String title, int focusIcon, int unfocusedIcon, View view) {
        NavigationBarItem item = new NavigationBarItem(mainView.getContext(), null,
                title, focusIcon, unfocusedIcon);
        item.setTag(view);
        item.setOnClickListener(this::changeFocus);
        int itemIndex = navigationBar.addItem(item);

        if (focusItem == null) {
            onFocus(item);
        }

        return itemIndex;
    }

    public <T extends View> T getContentViewByItemIndex(int itemIndex) {
        View view = navigationBar.getItem(itemIndex);
        //noinspection unchecked
        return (view != null) ? (T) view.getTag() : null;
    }

    private void changeFocus(View view) {
        NavigationBarItem item = (NavigationBarItem) view;
        if (focusItem != item) {
            missFocus(focusItem);
            onFocus(item);
        }
    }

    private void missFocus(NavigationBarItem item) {
        item.setFocus(false);
        mainView.removeView((View) item.getTag());
    }

    private void onFocus(NavigationBarItem item) {
        focusItem = item;
        item.setFocus(true);
        mainView.addView((View) item.getTag(), contentLayoutParams);
    }
}