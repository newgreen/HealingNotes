package com.zhengyp.healingnotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MoonNotesView extends ListView {
    public MoonNotesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapter(new Adapter(context));
    }

    private class Adapter extends ArrayAdapter<ItemData> {
        static final int resource = R.layout.moon_notes_item;

        Adapter(@NonNull Context context) {
            super(context, resource, getDataList(context));
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getViewSet(convertView).showData(getItem(position));
        }

        @NonNull
        private ViewSet getViewSet(View convertView) {
            ViewSet viewSet = null;
            if (convertView != null) {
                viewSet = (ViewSet)convertView.getTag();
            }

            if (viewSet == null) {
                viewSet = new ViewSet();
            }

            return viewSet;
        }

        class ViewSet {
            final ConstraintLayout layout;
            final ImageButton icon;
            final ViewGroup.LayoutParams iconLayoutParams;
            final TextView date;
            final ViewGroup.LayoutParams dateLayoutParams;
            final EditText moonChange;
            final ViewGroup.LayoutParams moonLayoutParams;
            final LinearLayout contentLayout;
            final ViewGroup.LayoutParams contentLayoutParams;
            final EditText innerContent;
            final EditText outerContent;
            ItemData data = null;

            ViewSet() {
                layout = (ConstraintLayout) LayoutInflater
                        .from(getContext()).inflate(resource, null);
                layout.setTag(this);

                icon = layout.findViewById(R.id.imgMoonNotesItemIcon);
                icon.setTag(this);
                icon.setOnClickListener(v -> {
                    ViewSet viewSet = (ViewSet)v.getTag();
                    if (viewSet != null && viewSet.icon == v && viewSet.data != null) {
                        viewSet.data.showContent = !viewSet.data.showContent;
                        showData(data);
                    }
                });
                iconLayoutParams = icon.getLayoutParams();

                moonChange = layout.findViewById(R.id.txtMoonNotesTitle);
                moonLayoutParams = moonChange.getLayoutParams();

                date = layout.findViewById(R.id.txtMoonNotesDate);
                dateLayoutParams = date.getLayoutParams();

                contentLayout = layout.findViewById(R.id.moonNotesContentLayout);
                contentLayoutParams = contentLayout.getLayoutParams();

                innerContent = contentLayout.findViewById(R.id.txtMoonNotesInner);
                outerContent = contentLayout.findViewById(R.id.txtMoonNotesOuter);
            }

            View showData(ItemData data) {
                if (data != null) {
                    this.data = data;

                    layout.removeView(icon);
                    layout.removeView(date);
                    layout.removeView(moonChange);
                    layout.removeView(contentLayout);

                    if (!data.date.isEmpty()) {
                        layout.addView(icon, iconLayoutParams);
                        layout.addView(date, dateLayoutParams);
                        layout.addView(moonChange, moonLayoutParams);

                        icon.setImageResource(data.getIcon());
                        date.setText(data.date);
                        moonChange.setText(data.moonChange);

                        if (data.showContent) {
                            innerContent.setText(data.innerContent);
                            outerContent.setText(data.outerContent);
                            layout.addView(contentLayout, contentLayoutParams);
                        }
                    }
                }

                return layout;
            }
        }
    }

    private final ArrayList<ItemData> dataList = new ArrayList<>();

    @NonNull
    private ArrayList<ItemData> getDataList(@NonNull Context context) {
        if (dataList.size() == 0) {
            dataList.add(new ItemData("2020-11-27 12:35", "正常 => 忧伤", "内部原因是……", "外部原因是……"));
            dataList.add(new ItemData("2020-11-28 12:35", "正常 => 愤怒", "内部原因是……", "外部原因是……"));
            dataList.add(new ItemData("2020-11-29 12:35", "正常 => 难过", "内部原因是……", "外部原因是……"));
            dataList.add(new ItemData("2020-11-30 12:35", "正常 => 郁闷", "内部原因是……", "外部原因是……"));
        }
        return dataList;
    }

    private static class ItemData {
        final String date;
        final String moonChange;
        final StringBuffer innerContent = new StringBuffer();
        final StringBuffer outerContent = new StringBuffer();
        boolean showContent = false;

        ItemData(String date, String moonChange, String innerContent, String outerContent) {
            this.date = date;
            this.moonChange = moonChange;
            this.innerContent.append(innerContent);
            this.outerContent.append(outerContent);
        }

        @DrawableRes
        int getIcon() {
            return showContent ? R.drawable.ic_arrow_drop_down : R.drawable.ic_arrow_right;
        }
    }
}