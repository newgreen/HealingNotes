package com.zhengyp.healingnotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListContent {
    ListContent() {
        // TODO:
    }
}

class ItemView extends View {
    /* ... white space ... 14%
     * ... ICON here   ... 46%
     * ... white space ...  4%
     * ... TITLE here  ... 18%
     * ... white space ... 18%
     */
    private static final int TOP_WHITE_SPACE = 14;
    private static final int ICON_SCALE = 46;
    private static final int MIDDLE_WHITE_SPACE = 4;
    private static final int TITLE_SCALE = 18;
    // private static final int BOTTOM_WHITE_SPACE = 18;

    private final String title;
    private final int focusIcon;
    private final int unfocusedIcon;
    private final int focusColor;
    private final boolean focus = false;

    private final Paint paint = new Paint();
    private final Rect canvasBounds = new Rect();
    private final Rect titleBounds = new Rect();

    public ItemView(Context context,
                    @Nullable AttributeSet attrs,
                    @NonNull String title,
                    @DrawableRes int focusIcon,
                    @DrawableRes int unfocusedIcon) {
        super(context, attrs);

        this.title = title;
        this.focusIcon = focusIcon;
        this.unfocusedIcon = unfocusedIcon;
        this.focusColor = context.getColor(R.color.focus_color);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,
                "原则", R.drawable.ic_all_inclusive_focus, R.drawable.ic_all_inclusive);
    }

    private int getIcon() {
        return focus ? focusIcon : unfocusedIcon;
    }

    private int getTextColor() {
        return focus ? focusColor : Color.BLACK;
    }

    private Typeface getTypeface() {
        return focus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getIcon() != 0) {
            drawIcon(canvas);
        }

        if (!title.isEmpty()) {
            drawTitle(canvas);
        }
    }

    private void drawIcon(Canvas canvas) {
        canvas.getClipBounds(canvasBounds);

        int squareSize = Math.min(canvasBounds.width(), canvasBounds.height());
        int iconSize = percentage(squareSize, ICON_SCALE);

        int x = canvasBounds.left + (canvasBounds.width() - iconSize) / 2;
        int y = canvasBounds.top + percentage(squareSize, TOP_WHITE_SPACE);

        Rect iconBounds = canvasBounds;
        iconBounds.set(x, y, x + iconSize, y + iconSize);

        // TODO:
        // Drawable image = ContextCompat.getDrawable(getContext(), getIcon());
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable image = getContext().getDrawable(getIcon());
        if (image != null) {
            image.setBounds(iconBounds);
            image.draw(canvas);
        }
    }

    private void drawTitle(Canvas canvas) {
        canvas.getClipBounds(canvasBounds);

        int squareSize = Math.min(canvasBounds.width(), canvasBounds.height());
        int titleHeight = percentage(squareSize, TITLE_SCALE);

        paint.setColor(getTextColor());
        paint.setTextSize(titleHeight);
        paint.setTypeface(getTypeface());
        paint.getTextBounds(title, 0, title.length(), titleBounds);
        int titleWidth = titleBounds.width();

        int x = canvasBounds.left + (canvasBounds.width() - titleWidth) / 2;
        int y = canvasBounds.top + (canvasBounds.height() - squareSize) / 2
                + percentage(squareSize, TOP_WHITE_SPACE, ICON_SCALE, MIDDLE_WHITE_SPACE)
                + titleHeight;
        canvas.drawText(title, x, y, paint);
    }

    private int percentage(int number, int... percentList) {
        int percentSum = 0;
        for (int percent:percentList) {
            percentSum += percent;
        }
        return number * percentSum / 100;
    }
}