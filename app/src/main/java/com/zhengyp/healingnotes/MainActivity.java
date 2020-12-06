package com.zhengyp.healingnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItem("原则1", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addItem("原则2", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
        addItem("原则3", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addItem("原则4", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
        addItem("原则5", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addItem("原则6", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
    }

    private void addItem(String title, int focusIcon, int unfocusedIcon) {
        ListContent listContent = findViewById(R.id.listContent);
        int ViewIndex = listContent.add(title, focusIcon, unfocusedIcon,
                android.R.layout.simple_list_item_1);
        TextView txtView = listContent.getContentView(ViewIndex);
        txtView.setText(title);
        txtView.setBackgroundColor(Color.LTGRAY);
    }
}