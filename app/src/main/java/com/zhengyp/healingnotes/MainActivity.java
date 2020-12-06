package com.zhengyp.healingnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPrincipleView();

        ListContent listContent = findViewById(R.id.listContent);
        listContent.add("原则A", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive,
                R.layout.principle_layout);

        addItem("原则1", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addItem("原则2", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
        addItem("原则3", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addItem("原则4", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);
        addItem("原则5", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
        addItem("原则6", R.drawable.ic_assignment_focus, R.drawable.ic_assignment);

        listContent.add("原则Z", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive,
                new PrincipleView(getApplicationContext(), null));
    }


    private void addItem(String title, int focusIcon, int unfocusedIcon) {
        ListContent listContent = findViewById(R.id.listContent);
        int ViewIndex = listContent.add(title, focusIcon, unfocusedIcon,
                android.R.layout.simple_list_item_1);
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

        principleView = layout.findViewById(R.id.principleView);

        ImageButton imgPrincipleMenu = layout.findViewById(R.id.imgPrincipleMenu);
        imgPrincipleMenu.setOnLongClickListener(this::onPrincipleMenuClick);
        imgPrincipleMenu.setOnClickListener(this::onPrincipleMenuClick);

        ImageButton imgPrincipleHome = layout.findViewById(R.id.imgPrincipleHome);
        imgPrincipleHome.setOnLongClickListener(this::redrawPrincipleView);
        imgPrincipleHome.setOnClickListener(this::redrawPrincipleView);
    }

    private boolean onPrincipleMenuClick(View view) {
        if (actionMode != null) {
            return false;
        }

        actionMode = startActionMode(actionModeCallback);
        view.setSelected(true);
        return true;
    }

    private boolean redrawPrincipleView(View view) {
        if (principleView != null) {
            principleView.redraw();
        }

        view.setSelected(true);
        return true;
    }

    private void redrawPrincipleView(boolean unfold) {
        if (principleView != null) {
            principleView.redraw(unfold);
        }
    }

    // Access the website for more information:
    // https://developer.android.com/guide/topics/ui/menus?hl=zh-cn#CAB
    PrincipleView principleView = null;
    ActionMode actionMode = null;
    ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.principle_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.principle_menu_fold) {
                redrawPrincipleView(false);
                return true;
            } else if (item.getItemId() == R.id.principle_menu_unfold) {
                redrawPrincipleView(true);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };
}