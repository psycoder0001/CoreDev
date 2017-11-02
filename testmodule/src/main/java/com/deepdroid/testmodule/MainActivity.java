package com.deepdroid.testmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.deepdroid.coredev.devdialog.DevelopmentDialog;
import com.deepdroid.coredev.devdialog.DevelopmentDialogListener;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;
import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlItem;
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
    }

    protected void initDevelopmentDialog(ViewGroup rootView) {
        DevUFO.getInstance(this, rootView, null, getDevelopmentDialogCustomOptionHelper());
        if (DevelopmentDialog.isDevelopmentEnabled(getApplicationContext()) && DevelopmentDialog.isDevelopmentStayAwakeEnabled(getApplicationContext())) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // Force device awake & screen on.
        }
    }

    public DevelopmentDialogListener getDevelopmentDialogCustomOptionHelper() {
        return new DevelopmentDialogListener() {
            @Override
            public boolean isDevelopmentVersion() {
                return false;
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
                return new SelectableServiceUrlData(new SelectableServiceUrlItem(0, 0, "UrlItemList 0",
                        "TestUrl Grp0 - Item0"
                        , "TestUrl Grp0 - Item1"
                        , "TestUrl Grp0 - Item2"
                )
                        , new SelectableServiceUrlItem(0, 0, "UrlItemList 1",
                        "TestUrl Grp1 - Item0"
                        , "TestUrl Grp1 - Item1"
                        , "TestUrl Grp1 - Item2"
                        , "TestUrl Grp1 - Item3"
                )
                        , new SelectableServiceUrlItem(0, 0, "UrlItemList 2",
                        "TestUrl Grp2 - Item0"
                        , "TestUrl Grp2 - Item1"
                )
                );
            }

            @Override
            public void onSelectionChanged(int newSelectionItemId, int newSelectionIndex, String newSelectionValue) {
                Log.println(Log.ASSERT, TAG, "Service url changed : "
                        + "\nitemID         : " + newSelectionItemId
                        + "\nSelectionIndex : " + newSelectionIndex
                        + "\nSelectionValue : " + newSelectionValue
                );
            }

            @Override
            public void onCustomCheckChanged(int index, boolean isChecked) {
                Log.println(Log.ASSERT, TAG, "CheckItemChanged index: " + index + " | isChecked : " + isChecked);
            }

            @Override
            public void onCustomButtonClicked(int index) {

            }
        };
    }
}
