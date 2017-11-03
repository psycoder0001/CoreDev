package com.deepdroid.coredev.corepicker;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.deepdroid.coredev.R;

import java.util.List;

/**
 * Created by evrenozturk on 23/06/15.
 */
public abstract class CorePicker extends Dialog {

    private static final String TAG = CorePicker.class.getSimpleName();
    private RecyclerView itemRv;

    private CorePickerConfiguration pickerConfiguration = new CorePickerConfiguration();
    private CorePickerListener corePickerListener;
    private List<String> textItemArray;

    private int selectedIndex = 0;

    protected abstract void getPickerConfiguration(CorePickerConfiguration pickerConfiguration);

    // =============================================================================================
    // CONSTRUCTOR & DIALOG OPERATIONS
    public CorePicker(Context context, List<String> textItemArray, CorePickerListener corePickerListener) {
        this(context, textItemArray, corePickerListener, 0.4f);
    }

    public CorePicker(Context context, List<String> textItemArray, CorePickerListener corePickerListener, final float dimAmount) {
        super(context, R.style.style_dialog_fullscreen);

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) getContext()).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ignored) {
            //
        }

        getPickerConfiguration(pickerConfiguration);
        if (pickerConfiguration == null) {
            Log.e(TAG, "Picker configurations cannot be empty");
        }
        if (pickerConfiguration.layoutResourceID < 1) {
            Log.e(TAG, "Picker configurations. InvalidLayoutID");
        }
        if (pickerConfiguration.listContainerID < 1) {
            Log.e(TAG, "Picker configurations. InvalidListContainerID");
        }

        this.textItemArray = textItemArray;
        this.corePickerListener = corePickerListener;

        try {
            if (getWindow() != null) {
                if (pickerConfiguration.customAnimStyleId != -1) {
                    getWindow().setWindowAnimations(pickerConfiguration.customAnimStyleId);
                } else {
                    getWindow().setWindowAnimations(R.style.customSpinnerDialogAnimation);
                }
            }

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Window window = getWindow();
//                    if (!TabletHelper.isTablet()) {
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.dimAmount = dimAmount;

                    window.setAttributes(params);
                    window.setBackgroundDrawableResource(android.R.color.transparent);
//                    } else {
//                        WindowManager.LayoutParams wmlp = window.getAttributes();
//                        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
//                    }
                }
            });
        } catch (IllegalStateException ignore) {
            // Caused by development mode dialog. Just ignore it.
        }
    }

    public void setScreenLocation(int gravity, int leftOffset, int topOffset) {
        Window window = getWindow();
        window.setGravity(gravity);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = leftOffset;
        params.y = topOffset;
        window.setAttributes(params);
    }

    public void showCustomDialog() {
        // If you get a bad token exception. Then you either you've inflated a layout with the ApplicationContext or you've created a CorePicker with ApplicationContext.
        // In that case use directly the activity to inflate or create the picker.
        try {
            show();
        } catch (WindowManager.BadTokenException e) {
            Log.e(TAG, "Bad Token - CorePicker can not work with applicationContext, try using directly an activity as context");
        }
    }

    public void dismissCustomDialog() {
        dismiss();
    }
    // CONSTRUCTOR & DIALOG OPERATIONS
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(pickerConfiguration.layoutResourceID);

        ViewGroup listContainerVg = (ViewGroup) findViewById(pickerConfiguration.listContainerID);

        itemRv = new RecyclerView(getContext());
        itemRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        itemRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        itemRv.setAdapter(recyclerViewAdapter);
        if (listContainerVg != null) {
            listContainerVg.addView(itemRv);
        }
    }

    private RecyclerView.Adapter recyclerViewAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(pickerConfiguration.itemResourceID, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).setData(textItemArray.get(position));
        }

        @Override
        public int getItemCount() {
            return textItemArray.size();
        }
    };

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;
        private View itemRoot;

        private TextView itemTextSelected;
        private View itemRootSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(pickerConfiguration.itemTextID);
            itemRoot = itemView.findViewById(pickerConfiguration.itemRootID);
            itemTextSelected = (TextView) itemView.findViewById(pickerConfiguration.itemSelectedTextID);
            itemRootSelected = itemView.findViewById(pickerConfiguration.itemSelectedRootID);
        }

        public void setData(String text) {
            if (getAdapterPosition() == selectedIndex) {
                itemTextSelected.setText(text);
                itemText.setText(text);
                if (itemRootSelected.getVisibility() != View.VISIBLE) {
                    itemRootSelected.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(itemRootSelected, "alpha", 0f, 1f).setDuration(400).start();
                }
            } else {
                itemTextSelected.setText(text);
                itemText.setText(text);
                if (itemRootSelected.getVisibility() == View.VISIBLE) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(itemRootSelected, "alpha", 1f, 0f).setDuration(400);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            itemRootSelected.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            itemRootSelected.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    animator.start();
                }
            }
            itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = getAdapterPosition();
                    if (itemRv != null && itemRv.getAdapter() != null) {
                        itemRv.getAdapter().notifyDataSetChanged();
                    }

                    if (corePickerListener != null) {
                        corePickerListener.onCorePickerConfirmed(selectedIndex);
                        dismissCustomDialog();
                    }
                }
            });
        }
    }
}