package io.github.ccmagic.movingfloatbutton;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by ccMagic on 2018/5/24.
 * Copyright ：
 * Version ：
 * Reference ：
 * Description ：
 */
public class FloatMovingLayout extends RelativeLayout {

    private SonView mView;
    private int screenX;
    private int screenY;

    private int scrollMaxX;
    private int scrollMaxY;
    //

    private boolean first = true;

    public FloatMovingLayout(Context context) {
        this(context, null);
    }


    public FloatMovingLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public FloatMovingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



        mView = new SonView(context);
        mView.setText("色块内滑动");
        mView.setGravity(Gravity.CENTER);
        mView.setLayoutParams(new LayoutParams(150, 150));
        mView.setBackgroundResource(R.drawable.button_bg_1);
        addView(mView);

    }

    public void parentScroll(int x, int y) {
        scrollTo(x, y);
    }

    public void getParentLocationOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        screenX = location[0];
        screenY = location[1];
    }

    private class SonView extends AppCompatTextView {

        private int actualX;
        private int actualY;

        private int allMinusX;
        private int allMinusY;

        public SonView(Context context) {
            this(context, null);
        }

        public SonView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        /**
         * @param context      The Context the view is running in, through which it can
         *                     access the current theme, resources, etc.
         * @param attrs        The attributes of the XML tag that is inflating the view.
         * @param defStyleAttr An attribute in the current theme that contains a
         *                     reference to a style resource that supplies default values for
         *                     the view. Can be 0 to not look for defaults.
         */
        public SonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getParentLocationOnScreen();
                    allMinusX = screenX + getMeasuredWidth() / 2;
                    allMinusY = screenY + getMeasuredHeight() / 2;
                    scrollMaxX = -FloatMovingLayout.this.getMeasuredWidth() + getMeasuredWidth();
                    scrollMaxY = -FloatMovingLayout.this.getMeasuredHeight() + getMeasuredHeight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    actualX = -(int) (event.getRawX() - allMinusX);
                    actualY = -(int) (event.getRawY() - allMinusY);
                    //防止滑动出界
                    if (actualX >= 0) {
                        actualX = 0;
                    }
                    if (actualY >= 0) {
                        actualY = 0;
                    }
                    if (actualX <= scrollMaxX) {
                        actualX = scrollMaxX;
                    }
                    if (actualY <= scrollMaxY) {
                        actualY = scrollMaxY;
                    }
                    parentScroll(actualX, actualY);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
