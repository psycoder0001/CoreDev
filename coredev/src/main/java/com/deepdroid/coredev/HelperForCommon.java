package com.deepdroid.coredev;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by evrenozturk on 16.05.2017.
 */

public class HelperForCommon {

    public static int getScreenWidth(Context context) {
        if (context == null) { return 0; }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) { return 0; }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Notifies(calls run function of the given runnable) when this view is measured & its ready to use.
     */
    public static void notifyWhenLayoutIsReady(final View view, final Runnable runnable) {
        if (view == null || runnable == null) {
            Log.e("HelperForCommon", "View or runnable is null\nnotifyWhenLayoutIsReady");
            return;
        }

        final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListenerSelfReference = this;

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListenerSelfReference);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListenerSelfReference);
                }
                runnable.run();
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }


    public static float dpToPx(int dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void restartWithInit(Context context) {
        Log.println(Log.ASSERT, "HelperForCommon", "Application is about to be restarted.");
        Intent mStartActivity = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 600, mPendingIntent);
        System.exit(0);
    }


    public static String getVersionName(Context context) {
        String appVersionName = "";
        try {
            String appPackageName = context.getPackageName();
            appVersionName = context.getPackageManager().getPackageInfo(appPackageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }
}
