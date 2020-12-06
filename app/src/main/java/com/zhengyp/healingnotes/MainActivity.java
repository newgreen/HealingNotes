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

        ListContent listContent = findViewById(R.id.listContent);
        int ViewIndex = listContent.add("原则1",
                R.drawable.ic_all_inclusive_focus,
                R.drawable.ic_all_inclusive,
                android.R.layout.simple_list_item_1);
        TextView txtView = listContent.getContentView(ViewIndex);
        txtView.setText("原则1");
        txtView.setBackgroundColor(Color.LTGRAY);

        ViewIndex = listContent.add("原则2",
                R.drawable.ic_assignment_focus,
                R.drawable.ic_assignment,
                android.R.layout.simple_list_item_1);
        txtView = listContent.getContentView(ViewIndex);
        txtView.setText("原则2");
    }
}