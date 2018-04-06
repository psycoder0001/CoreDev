package com.deepdroid.coredevtestmodule;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.deepdroid.coredev.corepicker.CorePickerItem;
import com.deepdroid.coredev.devdialog.DevelopmentDialog;
import com.deepdroid.coredev.devdialog.DevelopmentDialogData;
import com.deepdroid.coredev.devdialog.DevelopmentDialogListener;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlItem;
import com.deepdroid.coredev.devdialog.serviceurlselection.UrlSelectionItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentCheckItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentItem;
import com.deepdroid.coredev.devufo.DevUFO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        initDevelopmentDialog((ViewGroup) findViewById(R.id.main_root));
                    }
                }, 1000
        );
    }

    protected void initDevelopmentDialog(ViewGroup rootView) {
        DevelopmentDialogData devDialogData = new DevelopmentDialogData(true, true, false);
        DevUFO.attachUfo(this, rootView, null, getDevelopmentDialogCustomOptionHelper(), devDialogData);
        if (DevelopmentDialog.isDevelopmentEnabled(getApplicationContext()) && DevelopmentDialog.isDevelopmentStayAwakeEnabled(getApplicationContext())) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // Force device awake & screen on.
        }
    }

    public DevelopmentDialogListener getDevelopmentDialogCustomOptionHelper() {
        return new DevelopmentDialogListener() {
            @Override
            public boolean isDevelopmentVersion() {
                return true;
            }

            @Override
            public List<CustomDevelopmentItem> getCustomOptionsList() {
                List<CustomDevelopmentItem> list = new ArrayList<>();

                list.add(new CustomDevelopmentCheckItem(true, "TestItem 0"));
                list.add(new CustomDevelopmentCheckItem(true, "TestItem 1"));
                list.add(new CustomDevelopmentCheckItem(true, "TestItem 2"));

                return list;
            }

            @Override
            public SelectableServiceUrlData getServiceUrlLists() {
                return new SelectableServiceUrlData(getApplicationContext()
                        , new SelectableServiceUrlItem(0, 0, "UrlItemList 0"
                        , new CorePickerItem("Potato", "TestUrl Grp0 - Item0")
                        , new CorePickerItem("Tomato", "TestUrl Grp0 - Item1")
                        , new CorePickerItem("Garlic", "TestUrl Grp0 - Item2")
                )
                        , new SelectableServiceUrlItem(1, 2, "UrlItemList 1"
                        , new CorePickerItem("Orange", "TestUrl Grp1 - Item0")
                        , new CorePickerItem("Banana", "TestUrl Grp1 - Item1")
                        , new CorePickerItem("Cherry", "TestUrl Grp1 - Item2")
                        , new CorePickerItem("Kiwano", "TestUrl Grp1 - Item3")
                )
                        , new SelectableServiceUrlItem(2, 1, "UrlItemList 2"
                        , new CorePickerItem("Berry", "TestUrl Grp2 - Item0")
                        , new CorePickerItem("Melon", "TestUrl Grp2 - Item1")
                )
                );
            }

            @Override
            public void onSelectionChanged(UrlSelectionItem selectionItem) {
                Log.println(Log.ASSERT, TAG, "Service selectionValue changed : "
                        + "\nitemID         : " + selectionItem.itemId
                        + "\nSelectionIndex : " + selectionItem.selectionIndex
                        + "\nSelectionValue : " + selectionItem.selectionValue
                );
            }

            @Override
            public void onCustomCheckChanged(int index, boolean isChecked) {
                Log.println(Log.ASSERT, TAG, "CheckItemChanged index: " + index + " | isChecked : " + isChecked);
            }

            @Override
            public void onCustomButtonClicked(int index) {

            }

            @Override
            public View getCustomBackgroundView(ViewGroup parent) {
                return LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_custom_bg, parent, false);
            }
        };
    }
}
