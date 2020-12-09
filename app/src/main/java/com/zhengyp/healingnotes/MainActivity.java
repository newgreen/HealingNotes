package com.zhengyp.healingnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MainView mainView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = new MainView(findViewById(R.id.mainView));
        addPrincipleView();
        initNavigationBarDefaultItems(mainView);
    }

    private void addPrincipleView() {
        int itemIndex = mainView.addItem("原则",
                R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive,
                R.layout.principle_layout);
        LinearLayout layout = mainView.getContentViewByItemIndex(itemIndex);

        final PrincipleView principleView = layout.findViewById(R.id.principleView);

        ImageButton imgPrincipleMenu = layout.findViewById(R.id.imgPrincipleMenu);
        imgPrincipleMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.principle_menu_fold) {
                    principleView.setUnfold(false);
                    return true;
                } else if (menuItem.getItemId() == R.id.principle_menu_unfold) {
                    principleView.setUnfold(true);
                    return true;
                } else {
                    return false;
                }
            });
            popup.inflate(R.menu.principle_menu);
            popup.show();
        });

        ImageButton imgPrincipleHome = layout.findViewById(R.id.imgPrincipleHome);
        imgPrincipleHome.setOnClickListener(view -> principleView.goToTop());
    }

    private void initNavigationBarDefaultItems(MainView mainView) {
        addNavigationItem(mainView, "情绪笔记");
        addNavigationItem(mainView, "思维笔记");
    }

    private void addNavigationItem(MainView mainView, String title) {
        int itemIndex = mainView.addItem(title,
                R.drawable.ic_assignment_focus, R.drawable.ic_assignment, R.layout.simple_item);
        TextView textView = mainView.getContentViewByItemIndex(itemIndex);
        textView.setText(title);
    }
}