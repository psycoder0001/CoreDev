package com.deepdroid.testmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.deepdroid.coredev.devdialog.DevelopmentDialog;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.DevelopmentDialogListener;
import com.deepdroid.coredev.devufo.DevUFO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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
                return null;
            }

            @Override
            public String[] getServiceUrlList() {
                return new String[0];
            }

            @Override
            public String getServiceUrlAt(int selectedIndex) {
                return null;
            }

            @Override
            public String getCurrentServiceUrl() {
                return null;
            }

            @Override
            public void onServiceUrlChanged(String selectedServiceUrl) {

            }

            @Override
            public void onCustomCheckChanged(int index, boolean isChecked) {

            }

            @Override
            public void onCustomButtonClicked(int index) {

            }
        }
    }
}
