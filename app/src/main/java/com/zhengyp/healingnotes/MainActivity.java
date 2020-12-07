package com.zhengyp.healingnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPrincipleView();
        addItem("情绪笔记");
        // TODO: if comment the below, can not switch to the other view, WHY?
        addItem("思维笔记");
    }

    private void addItem(String title) {
        ListContent listContent = findViewById(R.id.listContent);
        int ViewIndex = listContent.add(title,
                R.drawable.ic_assignment_focus,
                R.drawable.ic_assignment,
                R.layout.simple_item);
        TextView txtView = listContent.getContentView(ViewIndex);
        txtView.setText(title);
        txtView.setBackgroundColor(Color.LTGRAY);
    }

    private void addPrincipleView() {
        ListContent listContent = findViewById(R.id.listContent);
        int itemIndex = listContent.add("原则",
                                        R.drawable.ic_all_inclusive_focus,
                                        R.drawable.ic_all_inclusive,
                                        R.layout.principle_layout);
        ConstraintLayout layout = listContent.getContentView(itemIndex);
        final PrincipleView principleView = layout.findViewById(R.id.principleView);

        ImageButton imgPrincipleMenu = layout.findViewById(R.id.imgPrincipleMenu);
        imgPrincipleMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.principle_menu_fold) {
                    principleView.redraw(false);
                    return true;
                } else if (menuItem.getItemId() == R.id.principle_menu_unfold) {
                    principleView.redraw(true);
                    return true;
                } else {
                    return false;
                }
            });
            popup.inflate(R.menu.principle_menu);
            popup.show();
        });

        ImageButton imgPrincipleHome = layout.findViewById(R.id.imgPrincipleHome);
        imgPrincipleHome.setOnClickListener(view -> principleView.redraw());
    }
}