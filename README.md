
# MovingFloatButton

### 项目介绍
自定义悬浮滑动按钮，支持在父布局中跟随手值滑动，提供了两种实现方案。

效果图：

![image](http://wx3.sinaimg.cn/large/bcc7d265gy1frudowju9ag20go0tnx2a.gif)


### 代码说明(实现方案)


#### 1、FloatMovingView
```
public class FloatMovingView extends AppCompatTextView {


    private static final String TAG = "FloatMovingView";
    private float mActualX;
    private float mActualY;

    private float mPreX;
    private float mPreY;

    private float mTranslationX;
    private float mTranslationY;
    private ViewGroup mParentViewGroup;
    private float mDefaultTopTranslationX;
    private float mDefaultTopTranslationY;
    private float mDefaultBottomTranslationX;
    private float mDefaultBottomTranslationY;
    private boolean mFirstMain = true;
    private long downCurrent;
    private OnClickListener mOnClickListener;

    public FloatMovingView(Context context) {
        this(context, null);
    }

    public FloatMovingView(Context context, @Nullable AttributeSet attrs) {
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
    public FloatMovingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downCurrent = System.currentTimeMillis();
                if (mFirstMain) {
                    mParentViewGroup = (ViewGroup) getParent();
                    //这四个数据用于防止移动出界
                    mDefaultTopTranslationX = getTranslationX() + getLeft();
                    mDefaultTopTranslationY = getTranslationY() + getTop();
                    //
                    mDefaultBottomTranslationX = mParentViewGroup.getMeasuredWidth() - getRight();
                    mDefaultBottomTranslationY = mParentViewGroup.getMeasuredHeight() - getBottom();
                    mFirstMain = false;
                }
                mPreX = event.getRawX();
                mPreY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mActualX = event.getRawX();
                mActualY = event.getRawY();
                mTranslationX = getTranslationX() + mActualX - mPreX;
                mTranslationY = getTranslationY() + mActualY - mPreY;
                //防止滑动出界
                if (mTranslationX < -mDefaultTopTranslationX) {
                    mTranslationX = -mDefaultTopTranslationX;
                }
                if (mTranslationY < -mDefaultTopTranslationY) {
                    mTranslationY = -mDefaultTopTranslationY;
                }

                if (mTranslationX > mDefaultBottomTranslationX) {
                    mTranslationX = mDefaultBottomTranslationX;
                }
                if (mTranslationY > mDefaultBottomTranslationY) {
                    mTranslationY = mDefaultBottomTranslationY;
                }
                setTranslationX(mTranslationX);
                setTranslationY(mTranslationY);
                //
                mPreX = mActualX;
                mPreY = mActualY;
                break;
            case MotionEvent.ACTION_UP:
                if (mOnClickListener != null && System.currentTimeMillis() - downCurrent < 200) {
                    mOnClickListener.onClick(this);
                }
                break;
            default:
                break;
        }
        return true;
    }

}
```
#### 2、FloatMovingLayout

```
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
        mView.setGravity(CENTER_IN_PARENT);
        mView.setLayoutParams(new LayoutParams(150, 150));
        mView.setBackgroundResource(R.mipmap.float_moving_btn);
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

```
