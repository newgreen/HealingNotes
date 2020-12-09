package com.zhengyp.healingnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationBar(new MainView(findViewById(R.id.mainView)));
    }

    private void initNavigationBar(MainView mainView) {
        addNavigationItem(mainView, "原则1", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem(mainView, "原则2", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
        addNavigationItem(mainView, "原则3", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem(mainView, "原则4", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
        addNavigationItem(mainView, "原则5", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem(mainView, "原则6", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
    }

    private void addNavigationItem(MainView mainView,
                                   String title, int focusIcon, int unfocusedIcon) {
        int itemIndex = mainView.addItem(title, focusIcon, unfocusedIcon, R.layout.simple_item);
        TextView textView = mainView.getContentViewByItemIndex(itemIndex);
        textView.setText(title);
    }
}