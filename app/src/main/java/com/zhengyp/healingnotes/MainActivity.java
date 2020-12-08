package com.zhengyp.healingnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private int navigationItemCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationBar();
    }

    private void initNavigationBar() {
        addNavigationItem("原则1", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem("原则2", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem("原则3", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem("原则4", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem("原则5", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addNavigationItem("原则6", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
    }

    private void addNavigationItem(String title, int focusIcon, int unfocusedIcon) {
        NavigationBarItem item = new NavigationBarItem(getApplicationContext(), null,
                title, focusIcon, unfocusedIcon);
        NavigationBar bar = findViewById(R.id.navigationBar);
        bar.addItem(item);

        item.setFocus(navigationItemCnt == 0);
        navigationItemCnt++;
    }
}