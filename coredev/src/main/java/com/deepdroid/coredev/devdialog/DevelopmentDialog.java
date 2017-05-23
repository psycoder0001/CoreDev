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
import android.widget.AbsoluteLayout;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepdroid.coredev.HelperForCommon;
import com.deepdroid.coredev.HelperForPref;
import com.deepdroid.coredev.R;
import com.deepdroid.coredev.corepicker.CorePicker;
import com.deepdroid.coredev.corepicker.CorePickerConfiguration;
import com.deepdroid.coredev.corepicker.CorePickerListener;
import com.deepdroid.coredev.customview.CoreDevListWithoutScroll;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentButtonItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentCheckItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.DevelopmentDialogCustomOptionsListener;

import java.util.List;

/**
 * Created by evrenozturk on 13/08/15.
 */
public class DevelopmentDialog extends Dialog {

    private static final String CURRENT_SERVICE_URL = "CURRENT_SERVICE_URL";
    private static final String DEVELOPMENT_MODE = "DEVELOPMENT_MODE";
    private static final String DEVELOPMENT_BUTTON_MODE = "DEVELOPMENT_BUTTON_MODE";
    private static final String DEVELOPMENT_AUTOFILL_MODE = "DEVELOPMENT_AUTOFILL_MODE";
    private static final String DEVELOPMENT_STAY_AWAKE_MODE = "DEVELOPMENT_STAY_AWAKE_MODE";

    private TextView versionName;
    private TextView valuesTypeName;

    // EDITABLE SERVICE LINK
    private CorePicker serviceLinkPicker;
    private EditText serviceUrlEt;
    private TextView serviceLinkSelectTv;

    // CHECKBOX
    private CheckBox enableDevCb;
    private TextView enableDevTv;
    private CheckBox showUfoCb;
    private TextView showUfoTv;
    private CheckBox autoFillCb;
    private TextView autoFillTv;
    private CheckBox stayAwakeCb;
    private TextView stayAwakeTv;

    // CUSTOM CHECKS
    private AbsoluteLayout customOptionsArea;

    // BOTTOM BUTTONS
    private TextView restartTv;
    private TextView okTv;

    private DevelopmentDialogCustomOptionsListener devDialogListener;


    public static DevelopmentDialog getInstance(Context context, DevelopmentDialogData developmentDialogData) {
        return new DevelopmentDialog(context, developmentDialogData);
    }

    private DevelopmentDialog(Context context, DevelopmentDialogData developmentDialogData) {
        super(context);
        devDialogListener = developmentDialogData.devDialogListener;
    }

    private Context getAppCx() {
        return getContext().getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_development);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(null);
        }

        View root = findViewById(R.id.development_root);
        fadeInAnimation(root);

        // EDITABLE SERVICE LINK
        versionName = (TextView) findViewById(R.id.development_application_version);
        serviceUrlEt = (EditText) findViewById(R.id.development_service_link_value);
        serviceLinkSelectTv = (TextView) findViewById(R.id.development_service_link_select);

        valuesTypeName = (TextView) findViewById(R.id.development_values_type);

        // SET SERVICE URL
        String currentServiceUrl = getCurrentServiceUrl(getContext());
        if (TextUtils.isEmpty(currentServiceUrl)) {
            currentServiceUrl = devDialogListener.getCurrentServiceUrl();
        }
        serviceUrlEt.setText(currentServiceUrl);
        serviceUrlEt.setSelection(currentServiceUrl.length() - 1);
        devDialogListener.onServiceUrlChanged(currentServiceUrl);

        // AWAY SERVERS
        serviceLinkPicker = new CorePicker(getContext(), devDialogListener.getServiceUrlList(), new CorePickerListener() {
            @Override
            public void onCorePickerCanceled(int selectedIndex) {}

            @Override
            public void onCorePickerConfirmed(int selectedIndex) {
                String selectedServiceUrl = devDialogListener.getServiceUrlAt(selectedIndex);

                if (!TextUtils.isEmpty(selectedServiceUrl)) {
                    // Set current selection
                    serviceUrlEt.setText(selectedServiceUrl);
                    setCurrentServiceUrl(getContext(), selectedServiceUrl);

                    // restart the application
                    devDialogListener.onServiceUrlChanged(selectedServiceUrl);
                    HelperForPref.setString(getAppCx(), CURRENT_SERVICE_URL, selectedServiceUrl);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() { HelperForCommon.restartWithInit(getAppCx()); }
                    });
                } else {
                    Toast.makeText(getAppCx(), "Service link cannot be empty!", Toast.LENGTH_LONG).show();
                }
            }
        }) {
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

        // CHECKBOX
        enableDevTv = (TextView) findViewById(R.id.development_mode_text);
        enableDevCb = (CheckBox) findViewById(R.id.development_mode_check);
        enableDevCb.setChecked(isDevelopmentEnabled(getAppCx()));
        enableDevCb.setOnCheckedChangeListener(mOnCheckChangedListener);

        showUfoTv = (TextView) findViewById(R.id.development_ufo_text);
        showUfoCb = (CheckBox) findViewById(R.id.development_ufo_check);
        showUfoCb.setChecked(isDevelopmentButtonEnabled(getAppCx()));
        showUfoCb.setOnCheckedChangeListener(mOnCheckChangedListener);

        autoFillTv = (TextView) findViewById(R.id.development_auto_fill_text);
        autoFillCb = (CheckBox) findViewById(R.id.development_auto_fill_check);
        autoFillCb.setChecked(isDevelopmentAutoFillEnabled(getAppCx()));
        autoFillCb.setOnCheckedChangeListener(mOnCheckChangedListener);

        stayAwakeTv = (TextView) findViewById(R.id.development_stay_awake_text);
        stayAwakeCb = (CheckBox) findViewById(R.id.development_stay_awake_check);
        stayAwakeCb.setChecked(isDevelopmentStayAwakeEnabled(getAppCx()));
        stayAwakeCb.setOnCheckedChangeListener(mOnCheckChangedListener);

        // CUSTOM CHECKS AREA
        customOptionsArea = (AbsoluteLayout) findViewById(R.id.development_custom_checks_area);

        // BOTTOM BUTTONS
        restartTv = (TextView) findViewById(R.id.development_restart_app);
        okTv = (TextView) findViewById(R.id.development_ok);

        serviceLinkSelectTv.setOnClickListener(mOnClickListener);
        enableDevTv.setOnClickListener(mOnClickListener);
        showUfoTv.setOnClickListener(mOnClickListener);
        autoFillTv.setOnClickListener(mOnClickListener);
        stayAwakeTv.setOnClickListener(mOnClickListener);
        restartTv.setOnClickListener(mOnClickListener);
        okTv.setOnClickListener(mOnClickListener);

        versionName.setText("v" + HelperForCommon.getVersionName(getAppCx()));
        valuesTypeName.setText("sw" + getAppCx().getResources().getInteger(R.integer.screen_type_int) + "dp");

        initializeCustomDevelopmentItems();
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == serviceLinkSelectTv) {
                serviceLinkPicker.showCustomDialog();
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
                if (getWindow() == null) { return; }
                if (isChecked) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // Force device awake & screen on.
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        }
    };

    // =========================================================================================
    // CUSTOM CHECK LIST
    private void initializeCustomDevelopmentItems() {
        if (devDialogListener == null) {
            return;
        }
        List<CustomDevelopmentItem> customOptionsList = devDialogListener.getCustomOptionsList();
        customOptionsArea.removeAllViews();
        CustomDevelopmentList customCheckList = new CustomDevelopmentList(getAppCx());
        customCheckList.inflateViews(getAppCx(), customOptionsList, R.layout.item_custom_development, (int) HelperForCommon.dpToPx(1, getAppCx()));
        customOptionsArea.addView(customCheckList);
    }

    private final class CustomDevelopmentList extends CoreDevListWithoutScroll {
        public CustomDevelopmentList(Context context) {
            super(context);
        }

        @Override
        protected void setDataAt(int index, View convertView, Object viewData) {
            CustomCheckViewHolder viewHolder;
            if (convertView.getTag() == null) {
                viewHolder = new CustomCheckViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomCheckViewHolder) convertView.getTag();
            }
            viewHolder.setData(viewData);
        }

        @Override
        protected void onItemClick(View view, int index) {
            if (view.getTag() != null && view.getTag() instanceof CustomCheckViewHolder) {
                Class dataType = ((CustomCheckViewHolder) view.getTag()).dataType;
                if (dataType == null || devDialogListener == null) {
                    return;
                }
                if (dataType == CustomDevelopmentButtonItem.class) {
                    // Button clicked.
                    devDialogListener.onCustomButtonClicked(index);
                } else if (dataType == CustomDevelopmentCheckItem.class) {
                    // Check changed.
                    ((CustomCheckViewHolder) view.getTag()).check.performClick();
                    devDialogListener.onCustomCheckChanged(index, ((CustomCheckViewHolder) view.getTag()).check.isChecked());
                }
            }
        }

        private class CustomCheckViewHolder {
            private View root;
            private CheckBox check;
            private TextView text;
            private Class dataType;

            private CustomCheckViewHolder(View convertView) {
                root = convertView.findViewById(R.id.development_item_custom_root);
                check = (CheckBox) convertView.findViewById(R.id.development_item_custom_check_check);
                text = (TextView) convertView.findViewById(R.id.development_item_custom_check_text);
            }

            public void setData(Object data) {
                if (data == null) {
                    return;
                }
                if (data instanceof CustomDevelopmentButtonItem) {
                    dataType = CustomDevelopmentButtonItem.class;
                    root.setBackgroundResource(R.drawable.dev_bg_button);
                    check.setVisibility(View.INVISIBLE);
                    text.setText(((CustomDevelopmentButtonItem) data).text);
                } else if (data instanceof CustomDevelopmentCheckItem) {
                    dataType = CustomDevelopmentCheckItem.class;
                    check.setChecked(((CustomDevelopmentCheckItem) data).isChecked);
                    text.setText(((CustomDevelopmentCheckItem) data).text);
                }
            }
        }
    }
    // CUSTOM CHECK LIST
    // =============================================================================================


    public void showCustomDialog() {
        // If you get a bad token exception. Then you either you've inflated a layout with the ApplicationContext or you've created a CorePicker with ApplicationContext.
        // In that case use directly the activity to inflate or create the picker.
        try {
            show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("BadToken", "CorePicker can not work with applicationContext, try using directly an activity as context");
        }
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


    // =============================================================================================
    // DEFAULT OPTIONS SET & QUERY METHODS
    public static void onDevelopmentVersion(Context applicationContext) {
        if (HelperForPref.getInt(applicationContext, DEVELOPMENT_MODE, 0) == 0) {
            setDevelopmentEnabled(applicationContext, true);
        }
        if (HelperForPref.getInt(applicationContext, DEVELOPMENT_BUTTON_MODE, 0) == 0) {
            setDevelopmentButtonEnabled(applicationContext, true);
        }
    }

    public static String getCurrentServiceUrl(Context applicationContext) {
        return HelperForPref.getStringValue(applicationContext, CURRENT_SERVICE_URL, "");
    }

    public static boolean isDevelopmentEnabled(Context applicationContext) {
        return HelperForPref.getInt(applicationContext, DEVELOPMENT_MODE, 0) == 1;
    }

    public static boolean isDevelopmentButtonEnabled(Context applicationContext) {
        return HelperForPref.getInt(applicationContext, DEVELOPMENT_BUTTON_MODE, 0) == 1;
    }

    public static boolean isDevelopmentAutoFillEnabled(Context applicationContext) {
        return HelperForPref.getBoolean(applicationContext, DEVELOPMENT_AUTOFILL_MODE, false);
    }

    public static boolean isDevelopmentStayAwakeEnabled(Context applicationContext) {
        return HelperForPref.getBoolean(applicationContext, DEVELOPMENT_STAY_AWAKE_MODE, false);
    }

    // SET
    private static void setCurrentServiceUrl(Context applicationContext, String serviceUrl) {
        HelperForPref.setString(applicationContext, CURRENT_SERVICE_URL, serviceUrl);
    }

    private static void setDevelopmentEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.setInt(applicationContext, DEVELOPMENT_MODE, newValue ? 1 : -1);
    }

    private static void setDevelopmentButtonEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.setInt(applicationContext, DEVELOPMENT_BUTTON_MODE, newValue ? 1 : -1);
    }

    private static void setDevelopmentAutoFillEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.setBoolean(applicationContext, DEVELOPMENT_AUTOFILL_MODE, newValue);
    }

    private static void setDevelopmentStayAwakeEnabled(Context applicationContext, boolean newValue) {
        HelperForPref.setBoolean(applicationContext, DEVELOPMENT_STAY_AWAKE_MODE, newValue);
    }
    // DEFAULT OPTIONS SET & QUERY METHODS
    // =============================================================================================
}