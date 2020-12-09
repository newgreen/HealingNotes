package com.zhengyp.healingnotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class PrincipleView extends ListView {
    private final Adapter adapter;

    public PrincipleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapter(adapter = new Adapter(context));
    }

    public void setUnfold(boolean unfold) {
        for (ItemData data : getDataList(getContext())) {
            data.showContent = unfold;
        }
        adapter.notifyDataSetChanged();
    }

    public void goToTop() {
        // access the website for more information:
        // https://stackoverflow.com/questions/12491855/android-listview-scrollto-doesnt-work
        adapter.notifyDataSetChanged();
        setSelection(0);
    }

    private class Adapter extends ArrayAdapter<ItemData> {
        static final int resource = R.layout.principle_item;

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
                viewSet = (ViewSet) convertView.getTag();
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
            final TextView title;
            final ViewGroup.LayoutParams titleLayoutParams;
            final TextView content;
            final ViewGroup.LayoutParams contentLayoutParams;
            ItemData data = null;

            ViewSet() {
                layout = (ConstraintLayout) LayoutInflater
                        .from(getContext()).inflate(resource, null);
                layout.setTag(this);

                icon = layout.findViewById(R.id.imgPrincipleItemIcon);
                iconLayoutParams = icon.getLayoutParams();
                icon.setTag(this);
                icon.setOnClickListener(v -> {
                    ViewSet viewSet = (ViewSet) v.getTag();
                    if (viewSet != null && viewSet.icon == v && viewSet.data != null) {
                        viewSet.data.showContent = !viewSet.data.showContent;
                        showData(data);
                    }
                });

                title = layout.findViewById(R.id.txtPrincipleItemTitle);
                titleLayoutParams = title.getLayoutParams();

                content = layout.findViewById(R.id.txtPrincipleItemContent);
                contentLayoutParams = content.getLayoutParams();
            }

            View showData(ItemData data) {
                if (data != null) {
                    this.data = data;

                    layout.removeView(icon);
                    layout.removeView(title);
                    layout.removeView(content);

                    if (!data.title.isEmpty()) {
                        icon.setImageResource(data.getIcon());
                        title.setText(data.title);

                        layout.addView(icon, iconLayoutParams);
                        layout.addView(title, titleLayoutParams);

                        if (data.showContent && data.content.length() > 0) {
                            content.setText(data.content);
                            layout.addView(content, contentLayoutParams);
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
            dataList.add(new ItemData());

            int principleCnt = context.getResources().getIntArray(R.array.principles).length;
            TypedArray principleIdList = context.getResources().obtainTypedArray(R.array.principles);
            for (int i = 0; i < principleCnt; i++) {
                // substring(1) to remove "@" and remain resourceId
                int stringArrayResId = Integer.parseInt(principleIdList.getString(i).substring(1));
                dataList.add(new ItemData(context.getResources().getStringArray(stringArrayResId)));
            }
            principleIdList.recycle();

            dataList.add(new ItemData());
        }

        return dataList;
    }

    private static class ItemData {
        final String title;
        final StringBuilder content = new StringBuilder();
        boolean showContent = false;

        ItemData(String[] data) {
            String indentSpace = "　　";

            title = data[0];
            for (int i = 1; i < data.length; i++) {
                content.append(indentSpace).append(data[i]).append("\n");
            }
        }

        ItemData() {
            this(new String[]{""});
        }

        @DrawableRes
        int getIcon() {
            return showContent ? R.drawable.ic_arrow_drop_down : R.drawable.ic_arrow_right;
        }
    }
}