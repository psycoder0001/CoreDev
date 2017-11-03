package com.deepdroid.coredev.devdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepdroid.coredev.HelperForCommon;
import com.deepdroid.coredev.HelperForPref;
import com.deepdroid.coredev.R;
import com.deepdroid.coredev.corepicker.CorePicker;
import com.deepdroid.coredev.corepicker.CorePickerConfiguration;
import com.deepdroid.coredev.corepicker.CorePickerListener;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlItem;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlList;
import com.deepdroid.coredev.devdialog.serviceurlselection.UrlSelectionItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentList;

import java.util.List;

/**
 * Created by evrenozturk on 13/08/15.
 */
public class DevelopmentDialog extends Dialog {

    private static final String DEVELOPMENT_MODE = "DEVELOPMENT_MODE";
    private static final String DEVELOPMENT_BUTTON_MODE = "DEVELOPMENT_BUTTON_MODE";
    private static final String DEVELOPMENT_AUTOFILL_MODE = "DEVELOPMENT_AUTOFILL_MODE";
    private static final String DEVELOPMENT_STAY_AWAKE_MODE = "DEVELOPMENT_STAY_AWAKE_MODE";
    private static final String TAG = DevelopmentDialog.class.getSimpleName();

    private View rootV;
    private TextView versionNameTv;
    private TextView valuesTypeNameTv;
    private RelativeLayout serviceLinkArea;
    private TextView setToDefaultTv;
    private TextView applyServiceLinkSelectionsTv;
    private CheckBox enableDevCb;
    private TextView enableDevTv;
    private CheckBox showUfoCb;
    private TextView showUfoTv;
    private CheckBox autoFillCb;
    private TextView autoFillTv;
    private CheckBox stayAwakeCb;
    private TextView stayAwakeTv;
    private RelativeLayout customOptionsArea;
    private TextView restartTv;
    private TextView okTv;

    private SelectableServiceUrlData selectableServiceUrlData;
    private DevelopmentDialogListener devDialogListener;
    private SelectableServiceUrlList selectableServiceUrlList;

    public static DevelopmentDialog getInstance(Context context, DevelopmentDialogData developmentDialogData) {
        return new DevelopmentDialog(context, developmentDialogData);
    }

    private DevelopmentDialog(Context context, DevelopmentDialogData developmentDialogData) {
        super(context);
        selectableServiceUrlData = developmentDialogData.selectableServiceUrlData;
        devDialogListener = developmentDialogData.devDialogListener;
    }

    private Context getAppCx() {
        return getContext().getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViews();
        fadeInAnimation(rootV);
        initializeViews();
        initializeSelectableServiceUrlList(false, true);
        initializeCustomDevelopmentItems();
    }

    // =========================================================================================
    // INITIALIZER METHODS
    private void findViews() {
        setCancelable(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_development);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(null);
        }

        rootV = findViewById(R.id.development_root);
        versionNameTv = findViewById(R.id.development_application_version);
        serviceLinkArea = findViewById(R.id.development_service_link_area);
        setToDefaultTv = findViewById(R.id.development_service_link_clear_selection);
        applyServiceLinkSelectionsTv = findViewById(R.id.development_service_link_apply_selection);
        valuesTypeNameTv = findViewById(R.id.development_values_type);
        enableDevTv = findViewById(R.id.development_mode_text);
        enableDevCb = findViewById(R.id.development_mode_check);
        showUfoTv = findViewById(R.id.development_ufo_text);
        showUfoCb = findViewById(R.id.development_ufo_check);
        autoFillTv = findViewById(R.id.development_auto_fill_text);
        autoFillCb = findViewById(R.id.development_auto_fill_check);
        stayAwakeTv = findViewById(R.id.development_stay_awake_text);
        stayAwakeCb = findViewById(R.id.development_stay_awake_check);
        customOptionsArea = findViewById(R.id.development_custom_checks_area);
        restartTv = findViewById(R.id.development_restart_app);
        okTv = findViewById(R.id.development_ok);
    }

    private void fadeInAnimation(final View rootView) {
        Animation scaleAnimationP1 = new ScaleAnimation(0f, 1f, 0.01f, 0.01f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimationP1.setDuration(400);
        scaleAnimationP1.setFillEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // We used handler because animation listener has a bug & it will not work on some 4... versions
                final Animation scaleAnimationP2 = new ScaleAnimation(1f, 1f, 0.01f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimationP2.setDuration(400);
                scaleAnimationP2.setFillEnabled(false);
                rootView.clearAnimation();
                rootView.startAnimation(scaleAnimationP2);
            }
        }, 390);
        rootView.startAnimation(scaleAnimationP1);
    }

    private void initializeViews() {
        // SET TEXTS
        versionNameTv.setText("v" + HelperForCommon.getVersionName(getAppCx()));
        valuesTypeNameTv.setText("sw" + getAppCx().getResources().getInteger(R.integer.screen_type_int) + "dp");
        // SET CH_BOX
        enableDevCb.setChecked(isDevelopmentEnabled(getAppCx()));
        showUfoCb.setChecked(isDevelopmentButtonEnabled(getAppCx()));
        autoFillCb.setChecked(isDevelopmentAutoFillEnabled(getAppCx()));
        stayAwakeCb.setChecked(isDevelopmentStayAwakeEnabled(getAppCx()));
        // SET LISTENERS
        enableDevCb.setOnCheckedChangeListener(mOnCheckChangedListener);
        showUfoCb.setOnCheckedChangeListener(mOnCheckChangedListener);
        autoFillCb.setOnCheckedChangeListener(mOnCheckChangedListener);
        stayAwakeCb.setOnCheckedChangeListener(mOnCheckChangedListener);
        setToDefaultTv.setOnClickListener(mOnClickListener);
        applyServiceLinkSelectionsTv.setOnClickListener(mOnClickListener);
        enableDevTv.setOnClickListener(mOnClickListener);
        showUfoTv.setOnClickListener(mOnClickListener);
        autoFillTv.setOnClickListener(mOnClickListener);
        stayAwakeTv.setOnClickListener(mOnClickListener);
        restartTv.setOnClickListener(mOnClickListener);
        okTv.setOnClickListener(mOnClickListener);
    }

    private void initializeSelectableServiceUrlList(boolean forceRecreateData, boolean forceRecreateUi) {
        if (devDialogListener == null) {
            Log.println(Log.ASSERT, TAG, "DevelopmentDialogListener was NULL");
            return;
        }
        if (selectableServiceUrlData == null || forceRecreateData) {
            // Get url list.
            selectableServiceUrlData = devDialogListener.getServiceUrlLists();
            if (selectableServiceUrlData.selectableServiceUrlItemList == null) {
                Log.println(Log.ASSERT, TAG, "selectableServiceUrlData.selectableServiceUrlItemList was NULL");
                return;
            }
            // Notify if previous selection exist.
            notifyServiceUrlSelectionChangedForAllItems(selectableServiceUrlData, devDialogListener);
        }

        if (forceRecreateUi) {
            serviceLinkArea.removeAllViews();
            selectableServiceUrlList = new SelectableServiceUrlList(getAppCx(), selectableServiceUrlListListener);
            selectableServiceUrlList.inflateViews(getAppCx(), selectableServiceUrlData.selectableServiceUrlItemList, R.layout.item_url_selection, 0);
            serviceLinkArea.addView(selectableServiceUrlList);
        }
    }

    private void initializeCustomDevelopmentItems() {
        if (devDialogListener == null) {
            return;
        }
        List<CustomDevelopmentItem> customOptionsList = devDialogListener.getCustomOptionsList();
        customOptionsArea.removeAllViews();
        CustomDevelopmentList customCheckList = new CustomDevelopmentList(getAppCx(), devDialogListener);
        customCheckList.inflateViews(getAppCx(), customOptionsList, R.layout.item_custom_development, 0);
        customOptionsArea.addView(customCheckList);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("BadToken", "DevDialog can not work with applicationContext, try using directly an activity as a context");
        }
    }
    // INITIALIZER METHODS
    // =========================================================================================

    // =========================================================================================
    // LISTENERS
    private final SelectableServiceUrlList.SelectableServiceUrlListListener selectableServiceUrlListListener = new SelectableServiceUrlList.SelectableServiceUrlListListener() {
        @Override
        public void onUrlChangeRequestedFor(int index) {
            showServiceUrlPicker(index);
        }
    };

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == setToDefaultTv) {
                HelperForPref.clearUrlSelections(getAppCx());
                initializeSelectableServiceUrlList(true, true);
                Log.println(Log.ASSERT, TAG, "All service url selections are now cleared");
                restartWithDelay();
            } else if (view == applyServiceLinkSelectionsTv) {
                applyServiceUrlSelections();
            } else if (view == enableDevTv) {
                enableDevCb.performClick();
            } else if (view == showUfoTv) {
                showUfoCb.performClick();
            } else if (view == autoFillTv) {
                autoFillCb.performClick();
            } else if (view == stayAwakeTv) {
                stayAwakeCb.performClick();
            } else if (view == restartTv) {
                HelperForCommon.restartWithInit(getAppCx());
            } else if (view == okTv) {
                dismiss();
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener mOnCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton == enableDevCb) {
                setDevelopmentEnabled(getAppCx(), isChecked);
            } else if (compoundButton == showUfoCb) {
                setDevelopmentButtonEnabled(getAppCx(), isChecked);
            } else if (compoundButton == autoFillCb) {
                setDevelopmentAutoFillEnabled(getAppCx(), isChecked);
            } else if (compoundButton == stayAwakeCb) {
                setDevelopmentStayAwakeEnabled(getAppCx(), isChecked);
                if (getWindow() == null) {
                    return;
                }
                if (isChecked) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // Force device awake & screen on.
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        }
    };
    // LISTENERS
    // =========================================================================================

    // =============================================================================================
    // DEFAULT OPTIONS SET & QUERY METHODS
    public static boolean isDevelopmentEnabled(Context applicationContext) {
        return HelperForPref.getInt(applicationContext, DEVELOPMENT_MODE, 1) == 1;
    }

    public static boolean isDevelopmentButtonEnabled(Context applicationContext) {
        return HelperForPref.getInt(applicationContext, DEVELOPMENT_BUTTON_MODE, 1) == 1;
    }

    public static boolean isDevelopmentAutoFillEnabled(Context applicationContext) {
        return HelperForPref.getBoolean(applicationContext, DEVELOPMENT_AUTOFILL_MODE, false);
    }

    public static boolean isDevelopmentStayAwakeEnabled(Context applicationContext) {
        return HelperForPref.getBoolean(applicationContext, DEVELOPMENT_STAY_AWAKE_MODE, false);
    }

    // SET
    private static void setDevelopmentEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.putInt(applicationContext, DEVELOPMENT_MODE, newValue ? 1 : -1);
    }

    private static void setDevelopmentButtonEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.putInt(applicationContext, DEVELOPMENT_BUTTON_MODE, newValue ? 1 : -1);
    }

    private static void setDevelopmentAutoFillEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.putBoolean(applicationContext, DEVELOPMENT_AUTOFILL_MODE, newValue);
    }

    private static void setDevelopmentStayAwakeEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.putBoolean(applicationContext, DEVELOPMENT_STAY_AWAKE_MODE, newValue);
    }

    // DEFAULT OPTIONS SET & QUERY METHODS
    // =============================================================================================

    // =============================================================================================
    // SERVICE URL SELECTION
    private void showServiceUrlPicker(final int index) {
        final CorePickerListener corePickerListener = new CorePickerListener() {
            @Override
            public void onCorePickerCanceled(int selectedIndex) {
            }

            @Override
            public void onCorePickerConfirmed(int selectionIndex) {
                if (devDialogListener == null) {
                    Log.println(Log.ASSERT, "DevelopmentDialog", "DevDialogListener was null");
                    return;
                }

                initializeSelectableServiceUrlList(false, false);
                UrlSelectionItem selectionItem = selectableServiceUrlData.setNewSelectionIndexAt(index, selectionIndex);
                if (selectionItem == null) {
                    return;
                }
                HelperForPref.putUrlSelection(getAppCx(), selectionItem);
                devDialogListener.onSelectionChanged(selectionItem);
                selectableServiceUrlList.notifyDataSetChanged();
            }
        };

        initializeSelectableServiceUrlList(false, false);
        CorePicker serviceLinkPicker = new CorePicker(getContext(), selectableServiceUrlData.getUrlListAt(index), corePickerListener) {
            @Override
            protected void getPickerConfiguration(CorePickerConfiguration pickerConfiguration) {
                pickerConfiguration.set(
                        R.layout.layout_core_picker,
                        R.id.core_picker_list_area,
                        0,
                        R.layout.item_core_picker,
                        R.id.core_picker_text,
                        R.id.core_picker_root,
                        R.id.core_picker_text_selected,
                        R.id.core_picker_root_selected
                );
            }
        };
        serviceLinkPicker.showCustomDialog();
    }

    public void applyServiceUrlSelections() {
        if (selectableServiceUrlData == null || selectableServiceUrlData.selectableServiceUrlItemList == null) {
            restartWithDelay();
            return;
        }
        int index = 0;
        for (SelectableServiceUrlItem selectableServiceUrlItem : selectableServiceUrlData.selectableServiceUrlItemList) {
            UrlSelectionItem urlSelectionItem = selectableServiceUrlItem.getSelectionItem();
            String customUrlValue = selectableServiceUrlList.geCustomUrlValueAt(index);
            if (urlSelectionItem == null || TextUtils.isEmpty(customUrlValue)) {
                Log.println(Log.ASSERT, TAG, "SelectionItem or customUrlValue was NULL. Ignoring onSelectionChanged for item : " + selectableServiceUrlItem.itemId);
                continue;
            }
            urlSelectionItem.selectionValue = customUrlValue;
            HelperForPref.putUrlSelection(getAppCx(), urlSelectionItem);
            index++;
        }
        restartWithDelay();
    }

    private void restartWithDelay() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                HelperForCommon.restartWithInit(getAppCx());
            }
        });
    }

    public static void notifyServiceUrlSelectionChangedForAllItems(SelectableServiceUrlData selectableServiceUrlData, DevelopmentDialogListener devDialogListener) {
        for (SelectableServiceUrlItem selectableServiceUrlItem : selectableServiceUrlData.selectableServiceUrlItemList) {
            if (selectableServiceUrlItem.getSelectionItem() == null) {
                Log.println(Log.ASSERT, TAG, "SelectionItem was NULL. Ignoring onSelectionChanged for item : " + selectableServiceUrlItem.itemId);
                return;
            }
            devDialogListener.onSelectionChanged(selectableServiceUrlItem.getSelectionItem());
        }
    }

    // SERVICE URL SELECTION
    // =============================================================================================

    // =============================================================================================
    // STATIC INITIALIZER
    public static void onDevelopmentVersion(Context applicationContext) {
        if (HelperForPref.getInt(applicationContext, DEVELOPMENT_MODE, 0) == 0) {
            setDevelopmentEnabled(applicationContext, true);
        }
        if (HelperForPref.getInt(applicationContext, DEVELOPMENT_BUTTON_MODE, 0) == 0) {
            setDevelopmentButtonEnabled(applicationContext, true);
        }
    }
    // STATIC INITIALIZER
    // =============================================================================================
}