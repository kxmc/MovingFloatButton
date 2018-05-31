package io.github.ccmagic.movingfloatbutton;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by ccMagic on 2018/5/31.
 * Copyright ：
 * Version ：
 * Reference ：
 * Description ：
 */
public class NavAndStatusutil {

    /**
     * 透明状态栏和导航栏
     */
    public static void setTransparentBar(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (navigationBarExist(activity)) {
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            decorView.setSystemUiVisibility(option);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);
            //
            decorView.addView(createStatusBarView(activity, Color.TRANSPARENT), 0);
            if (navigationBarExist(activity)) {
                decorView.addView(createNavBarView(activity, Color.TRANSPARENT), 1);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(createStatusBarView(activity, Color.TRANSPARENT), 0);
            if (navigationBarExist(activity)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.addView(createNavBarView(activity, Color.TRANSPARENT), 1);
            }
        }
    }

    /**
     * 判断导航栏是否存在
     * <p>
     * getRealMetrics()和getMetrics()获取到的屏幕信息差别只在于widthPixels或heightPixels的值是否去除虚拟键所占用的像素，和是否全屏和沉浸模式无关
     * 具体到问题就是用getMetrics()获取到的是1280x672，而用getRealMetrics()获取到的就是正确的1280x720
     */
    private static boolean navigationBarExist(AppCompatActivity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        //Android.util 包下的DisplayMetrics 类提供了一种关于显示的通用信息，如显示大小，分辨率和字体
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);//将当前窗口的一些信息放在DisplayMetrics类中,然后就可以通过displayMetrics类来获取当前窗口的一些信息
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);//将当前窗口的一些信息放在DisplayMetrics类中,然后就可以通过displayMetrics类来获取当前窗口的一些信息

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }


    /**
     * 重新创建状态栏填充的View
     */
    private static View createStatusBarView(AppCompatActivity activity, @ColorInt int color) {
        View statusBarTintView = new View(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight1(activity));
        params.gravity = Gravity.TOP;
        statusBarTintView.setLayoutParams(params);
        statusBarTintView.setBackgroundColor(color);
        return statusBarTintView;
    }

    /**
     * 获取状态栏的高度
     */
    private static int getStatusBarHeight1(AppCompatActivity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 重新创建导航栏填充的View
     */
    private static View createNavBarView(AppCompatActivity activity, @ColorInt int color) {
        View navBarTintView = new View(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getNavigationHeight(activity));
        params.gravity = Gravity.BOTTOM;
        navBarTintView.setLayoutParams(params);
        navBarTintView.setBackgroundColor(color);
        return navBarTintView;
    }

    /**
     * 获取导航栏的高度
     */
    private static int getNavigationHeight(AppCompatActivity activity) {
        int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }
}
