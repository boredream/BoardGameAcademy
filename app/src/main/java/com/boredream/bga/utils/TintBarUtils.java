package com.boredream.bga.utils;

/**
 * Created by Li on 2017/3/23.
 */

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 设置状态栏颜色的工具类
 */
public class TintBarUtils {

    public static void setWindowStatusBar(Activity activity, int bgColorResId, boolean lightMode, boolean fullScreen) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        try {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option = decorView.getSystemUiVisibility();

            // 全屏
            //if (fullScreen) {
            //    option = option | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //}

            option = option | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            // 状态栏文字颜色模式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (lightMode) {
                    option = option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            } else {
                MIUISetStatusBarLightMode(window, lightMode);
                FlymeSetStatusBarLightMode(window, lightMode);
            }

            decorView.setSystemUiVisibility(option);

            // 状态栏背景
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(bgColorResId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格 * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口 * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                    // }}
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
