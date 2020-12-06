package com.zhengyp.healingnotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListContent extends SimpleLayout {
    private final HorizontalLayout contentContainer;
    private final HorizontalLayout navigationBar;
    private final int navigationBarHeight;
    private ItemView focusItem = null;

    private final HorizontalScrollDetector.HorizontalScrollListener scrollListener =
            new HorizontalScrollDetector.HorizontalScrollListener() {
                @Override
                public void onScroll(int x) {
                    navigationBar.scrollBy(x);
                }
            };

    public ListContent(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ListContent);
        navigationBarHeight = (int) array.getDimension(
                R.styleable.ListContent_navigationBarHeight, dp2px(60));
        int navigationMaxItemToShow = array.getInteger(
                R.styleable.ListContent_maxItemCntToShow, 4);
        array.recycle();

        // add contentContainer firstly
        add(contentContainer = new HorizontalLayout(context, attrs) {
            @Override
            protected int getMaxItemCntToShow() {
                return 1;
            }

            @Override
            void scrollTo(View view) {
                int offset = 0;
                for (int i = 0; i < viewList.size(); i++, offset += itemWidth + margin) {
                    if (view == viewList.get(i)) {
                        scrollTo(offset, getScrollY());
                        break;
                    }
                }
            }
        });

        add(navigationBar = new HorizontalLayout(context, attrs) {
            private int maxScrollOffset;

            @Override
            protected void calculateInnerLayoutParams() {
                super.calculateInnerLayoutParams();
                maxScrollOffset = (itemCnt >= viewList.size()) ? 0 :
                        (viewList.size() - itemCnt) * (itemWidth + margin);
            }

            @Override
            protected int getMaxItemCntToShow() {
                return navigationMaxItemToShow;
            }

            @Override
            void scrollBy(int x) {
                if (getScrollX() + x <= 0) {
                    scrollTo(0, getScrollY());
                } else if (getScrollX() + x >= maxScrollOffset) {
                    scrollTo(maxScrollOffset, getScrollY());
                } else {
                    scrollBy(x, 0);
                }
            }

            @Override
            boolean scrollEnable() {
                return maxScrollOffset > 0;
            }
        });
        navigationBar.setBackgroundColor(Color.LTGRAY);
    }

    public int add(@NonNull String title,
                   @DrawableRes int focusIcon,
                   @DrawableRes int unfocusedIcon,
                   @LayoutRes int resId) {
        return add(title, focusIcon, unfocusedIcon,
                LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public int add(@NonNull String title,
                   @DrawableRes int focusIcon,
                   @DrawableRes int unfocusedIcon,
                   @NonNull View contentView) {
        ItemView itemView = new ItemView(getContext(),
                title, focusIcon, unfocusedIcon, scrollListener);
        itemView.setTag(contentView);

        int viewIndex = contentContainer.add(contentView);
        navigationBar.add(itemView);

        if (viewIndex == 0) {
            changeFocus(itemView);
        }
        return viewIndex;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getContentView(int viewIndex) {
        return (T) contentContainer.getView(viewIndex);
    }

    @Override
    protected LayoutParams getLayoutParams(int viewIndex) {
        final int CONTENT_CONTAINER_VIEW_INDEX = 0;
        final int NAVIGATION_BAR_VIEW_INDEX = 1;

        LayoutParams layoutParams;
        int margin = dp2px(1);

        switch (viewIndex) {
            case CONTENT_CONTAINER_VIEW_INDEX:
                int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom()
                        - margin - navigationBarHeight;
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                layoutParams.bottomMargin = margin;
                break;

            case NAVIGATION_BAR_VIEW_INDEX:
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, navigationBarHeight);
                layoutParams.addRule(BELOW, viewList.get(CONTENT_CONTAINER_VIEW_INDEX).getId());
                break;

            default:
                layoutParams = null;
                break;
        }

        return layoutParams;
    }

    private void changeFocus(ItemView view) {
        if (focusItem != null) {
            focusItem.setFocus(false);
        }

        focusItem = view;
        focusItem.setFocus(true);
        contentContainer.scrollTo((View)focusItem.getTag());
    }

    private class ItemView extends View {
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
        private final int backgroundColor;
        private boolean focus = false;

        private final Paint paint = new Paint();
        private final Rect canvasBounds = new Rect();
        private final Rect titleBounds = new Rect();

        private final SelectDetector selectDetector = new SelectDetector(this) {
            @Override
            void onSelecting() {
                setBackgroundColor(Color.LTGRAY);
            }

            @Override
            void onSelected(boolean selected) {
                setBackgroundColor(backgroundColor);
                changeFocus(ItemView.this);
            }
        };

        private final HorizontalScrollDetector scrollDetector;

        ItemView(Context context,
                 @NonNull String title,
                 @DrawableRes int focusIcon,
                 @DrawableRes int unfocusedIcon,
                 @NonNull HorizontalScrollDetector.HorizontalScrollListener listener) {
            super(context);

            this.title = title;
            this.focusIcon = focusIcon;
            this.unfocusedIcon = unfocusedIcon;
            this.focusColor = context.getColor(R.color.focus_color);
            this.backgroundColor = getDrawingCacheBackgroundColor();
            this.scrollDetector = new HorizontalScrollDetector(listener);
        }

        void setFocus(boolean focus) {
            if (focus != this.focus) {
                this.focus = focus;
                invalidate();
            }
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

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            selectDetector.onTouchEvent(event);
            if (navigationBar.scrollEnable()) {
                scrollDetector.onTouchEvent(event);
            } else {
                Log.i("SCROLL", "scroll disable");
            }
            return true;
        }
    }

    private static abstract class HorizontalLayout extends SimpleLayout {
        protected int itemCnt;
        protected int margin;
        protected int itemWidth;
        protected int itemHeight;

        HorizontalLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void calculateInnerLayoutParams() {
            itemCnt = Math.min(viewList.size(), getMaxItemCntToShow());
            margin = dp2px(1);
            itemWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()
                    - (itemCnt - 1) * margin) / itemCnt;
            itemHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        }

        @Override
        protected LayoutParams getLayoutParams(int viewIndex) {
            LayoutParams params = new LayoutParams(itemWidth, itemHeight);
            if (viewIndex != viewList.size() - 1) {
                params.rightMargin = margin;
            }
            if (viewIndex != 0) {
                params.addRule(RIGHT_OF, viewList.get(viewIndex - 1).getId());
            }
            return params;
        }

        void scrollTo(View view) {}
        void scrollBy(int x) {}
        boolean scrollEnable() { return false; }
        abstract protected int getMaxItemCntToShow();
    }

    private static abstract class SelectDetector {
        private final View view;
        private boolean press = false;

        SelectDetector(View view) {
            this.view = view;
        }

        abstract void onSelecting();
        abstract void onSelected(boolean selected);

        void onTouchEvent(MotionEvent event) {
            boolean lastPress = press;
            if (event.getAction() == MotionEvent.ACTION_DOWN && isInArea(event)) {
                press = true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP || !isInArea(event)) {
                press = false;
            }

            if (!lastPress && press) {
                onSelecting();
            }

            if (lastPress && !press) {
                boolean selected = event.getAction() == MotionEvent.ACTION_UP && isInArea(event);
                onSelected(selected);
            }
        }

        private boolean isInArea(MotionEvent event) {
            float w = view.getWidth();
            float h = view.getHeight();
            float x = event.getX(0);
            float y = event.getY(0);
            return x >= 0 && x < w && y >= 0 && y <= h;
        }
    }

    private static class HorizontalScrollDetector {
        interface HorizontalScrollListener {
            void onScroll(int x);
        }

        private boolean isSet = false;
        private float startX;
        private long startEventTime;
        private final HorizontalScrollListener listener;

        HorizontalScrollDetector(@NonNull HorizontalScrollListener listener) {
            this.listener = listener;
        }

        void onTouchEvent(MotionEvent event) {
            final int SCROLL_PRECISION = 10;
            final int SCROLL_DURATION = 50;

            boolean lastSet = isSet;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX(0);
                    startEventTime = event.getEventTime();
                    isSet = true;
                    break;

                case MotionEvent.ACTION_UP:
                    isSet = false;
                    // fall-through

                case MotionEvent.ACTION_MOVE:
                    if (lastSet) {
                        float move = startX - event.getX(0);
                        if ((move >= SCROLL_PRECISION
                                && event.getEventTime() - startEventTime >= SCROLL_DURATION)
                                || event.getAction() == MotionEvent.ACTION_UP) {
                            listener.onScroll((int)move);
                            startX = event.getX(0);
                        }
                    }
                    break;
            }
        }
    }
}

abstract class SimpleLayout extends RelativeLayout {
    protected final ArrayList<View> viewList = new ArrayList<>();
    protected boolean needRefresh = true;

    public SimpleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected int add(View view) {
        int viewIndex = viewList.size();

        view.setId(viewIndex + 1); // id start from 1
        viewList.add(view);
        refresh();

        return viewIndex;
    }

    protected View getView(int viewIndex) {
        return viewIndex >= 0 && viewIndex < viewList.size() ?  viewList.get(viewIndex) : null;
    }

    protected void refresh() {
        needRefresh = true;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureLayout();
    }

    protected void measureLayout() {
        if (needRefresh) {
            needRefresh = false;

            if (viewList.size() > 0) {
                for (View view : viewList) {
                    removeView(view);
                }

                calculateInnerLayoutParams();
                for (int i = 0; i < viewList.size(); i++) {
                    addView(viewList.get(i), getLayoutParams(i));
                }
            }
        }
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }

    protected void calculateInnerLayoutParams() {}
    protected abstract LayoutParams getLayoutParams(int viewIndex);
}