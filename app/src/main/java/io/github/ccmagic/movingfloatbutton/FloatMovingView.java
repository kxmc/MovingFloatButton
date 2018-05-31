package io.github.ccmagic.movingfloatbutton;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by ccMagic on 2018/5/24.
 * Copyright ：
 * Version ：
 * Reference ：
 * Description ：
 */
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

                //防止移动出界
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
