package com.deepdroid.coredev.devufo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.deepdroid.coredev.HelperForCommon;
import com.deepdroid.coredev.R;
import com.deepdroid.coredev.devdialog.DevelopmentDialog;
import com.deepdroid.coredev.devdialog.DevelopmentDialogData;
import com.deepdroid.coredev.devdialog.DevelopmentDialogListener;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;

/**
 * Created by evrenozturk on 13/08/15.
 */
public class DevUFO extends AppCompatImageView {

    private static final boolean IS_ENTER_ANIMATION_ENABLED = false;
    private FrameLayout.LayoutParams mFrameLayoutLayoutParams;
    private RelativeLayout.LayoutParams mRelativeLayoutLayoutParams;
    private int mDeltaX;
    private int mDeltaY;
    private int mUfoDiameter;


    private DevUFO(Context context) {
        super(context);
        init();
    }

    private DevUFO(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private DevUFO(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mUfoDiameter = (int) HelperForCommon.dpToPx(50, getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation((int) HelperForCommon.dpToPx(10, getContext()));
        }
        if (DevelopmentDialog.isDevelopmentEnabled(getContext()) && DevelopmentDialog.isDevelopmentButtonEnabled(getContext())) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }

        setBackgroundResource(R.drawable.dev_button_blackdroid);
        setImageResource(R.drawable.dev_blackdroid);
        if (isInEditMode()) {
            return;
        }
        HelperForCommon.notifyWhenLayoutIsReady(this, new Runnable() {
            @Override
            public void run() {
                getLayoutParams().width = mUfoDiameter;
                getLayoutParams().height = mUfoDiameter;

                setClickable(true);
                setFocusableInTouchMode(true);

                if (getParent() != null && getParent() instanceof FrameLayout) {
                    mFrameLayoutLayoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                    int leftMargin = (int) HelperForCommon.dpToPx(25, getContext());
                    int topMargin = ((FrameLayout) getParent()).getHeight() - (int) HelperForCommon.dpToPx(75, getContext()) - mUfoDiameter;
                    mFrameLayoutLayoutParams.setMargins(leftMargin, topMargin, 0, 0);
                    mFrameLayoutLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                    mRelativeLayoutLayoutParams = null;
                } else if (getParent() != null && getParent() instanceof RelativeLayout) {
                    mRelativeLayoutLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                    int leftMargin = (int) HelperForCommon.dpToPx(25, getContext());
                    int topMargin = ((RelativeLayout) getParent()).getHeight() - (int) HelperForCommon.dpToPx(75, getContext()) - mUfoDiameter;
                    mRelativeLayoutLayoutParams.setMargins(leftMargin, topMargin, 0, 0);
                    mFrameLayoutLayoutParams = null;
                } else {
                    mFrameLayoutLayoutParams = null;
                    mRelativeLayoutLayoutParams = null;
                }
                animatedFadeIn();
            }
        });
    }

    private void animatedFadeIn() {
        if (IS_ENTER_ANIMATION_ENABLED) {
            ObjectAnimator.ofFloat(DevUFO.this, "alpha", 0f, 1f).setDuration(400);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mFrameLayoutLayoutParams == null && mRelativeLayoutLayoutParams == null) {
            return super.onTouchEvent(event);
        }
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mFrameLayoutLayoutParams != null) {
                    mDeltaX = X - mFrameLayoutLayoutParams.leftMargin;
                    mDeltaY = Y - mFrameLayoutLayoutParams.topMargin;
                }
                if (mRelativeLayoutLayoutParams != null) {
                    mDeltaX = X - mRelativeLayoutLayoutParams.leftMargin;
                    mDeltaY = Y - mRelativeLayoutLayoutParams.topMargin;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mFrameLayoutLayoutParams != null) {
                    mFrameLayoutLayoutParams.leftMargin = X - mDeltaX;
                    mFrameLayoutLayoutParams.topMargin = Y - mDeltaY;
                    mFrameLayoutLayoutParams.rightMargin = -250;
                    mFrameLayoutLayoutParams.bottomMargin = -250;

                    if (mFrameLayoutLayoutParams.leftMargin < 0) {
                        mFrameLayoutLayoutParams.leftMargin = 0;
                    }
                    if (mFrameLayoutLayoutParams.topMargin < 0) {
                        mFrameLayoutLayoutParams.topMargin = 0;
                    }
                    if (mFrameLayoutLayoutParams.leftMargin + getWidth() > HelperForCommon.getScreenWidth(getContext())) {
                        mFrameLayoutLayoutParams.leftMargin = HelperForCommon.getScreenWidth(getContext()) - getWidth();
                    }
                    if (mFrameLayoutLayoutParams.topMargin + getHeight() > HelperForCommon.getScreenHeight(getContext())) {
                        mFrameLayoutLayoutParams.topMargin = HelperForCommon.getScreenHeight(getContext()) - getHeight();
                    }
                    setLayoutParams(mFrameLayoutLayoutParams);
                } else {
                    mRelativeLayoutLayoutParams.leftMargin = X - mDeltaX;
                    mRelativeLayoutLayoutParams.topMargin = Y - mDeltaY;
                    mRelativeLayoutLayoutParams.rightMargin = -250;
                    mRelativeLayoutLayoutParams.bottomMargin = -250;

                    if (mRelativeLayoutLayoutParams.leftMargin < 0) {
                        mRelativeLayoutLayoutParams.leftMargin = 0;
                    }
                    if (mRelativeLayoutLayoutParams.topMargin < 0) {
                        mRelativeLayoutLayoutParams.topMargin = 0;
                    }
                    if (mRelativeLayoutLayoutParams.leftMargin + getWidth() > HelperForCommon.getScreenWidth(getContext())) {
                        mRelativeLayoutLayoutParams.leftMargin = HelperForCommon.getScreenWidth(getContext()) - getWidth();
                    }
                    if (mRelativeLayoutLayoutParams.topMargin + getHeight() > HelperForCommon.getScreenHeight(getContext())) {
                        mRelativeLayoutLayoutParams.topMargin = HelperForCommon.getScreenHeight(getContext()) - getHeight();
                    }
                    setLayoutParams(mRelativeLayoutLayoutParams);
                }
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public static void attachUfo(final Activity activityContext
            , final ViewGroup rootView
            , final OnClickListener externalOnClickListenerForUFO
            , final DevelopmentDialogListener developmentDialogListener
            , final DevelopmentDialogData developmentDialogData) {
        try {
            if (developmentDialogListener == null) {
                Log.println(Log.ASSERT, "DevUFO", "DevelopmentDialogListener cannot be null");
                return;
            }
            if (!developmentDialogListener.isDevelopmentVersion()) {
                Log.println(Log.ASSERT, "DevUFO", "This app is not development version");
                return;
            }
            if (developmentDialogListener.isDevelopmentVersion()) {
                DevelopmentDialog.onDevelopmentVersion(activityContext.getApplicationContext());
            }
            if (!DevelopmentDialog.isDevelopmentEnabled(activityContext.getApplicationContext())) {
                Log.println(Log.ASSERT, "DevUFO", "This app is development version but the development mode is disabled manually");
                return;
            }
            if (!DevelopmentDialog.isDevelopmentButtonEnabled(activityContext.getApplicationContext())) {
                Log.println(Log.ASSERT, "DevUFO", "This app is development version but the development button is disabled manually");
                return;
            }

            final SelectableServiceUrlData selectableServiceUrlData = developmentDialogListener.getServiceUrlLists();
            DevelopmentDialog.notifyServiceUrlSelectionChangedForAllItems(selectableServiceUrlData, developmentDialogListener);

            DevUFO devUFO = new DevUFO(activityContext);
            devUFO.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // SHOW DEVELOPMENT DIALOG
                    DevelopmentDialog developmentDialog = DevelopmentDialog.getInstance(activityContext
                            , selectableServiceUrlData
                            , developmentDialogListener
                            , developmentDialogData);
                    developmentDialog.show();

                    // INFORM EVENT TO EXTERNAL LISTENER
                    if (externalOnClickListenerForUFO != null) {
                        externalOnClickListenerForUFO.onClick(v);
                    }
                }
            });
            rootView.addView(devUFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
