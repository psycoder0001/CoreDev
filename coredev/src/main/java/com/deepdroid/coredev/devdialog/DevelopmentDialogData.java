package com.deepdroid.coredev.devdialog;

import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public class DevelopmentDialogData {
    protected SelectableServiceUrlData selectableServiceUrlData;
    protected DevelopmentDialogListener devDialogListener;
    public boolean isOkButtonEnabled = false;
    public boolean isRestartButtonEnabled = false;
    public boolean isBackPressEnabled = true;
    public String versionName;

    public DevelopmentDialogData(boolean isOkButtonEnabled, boolean isRestartButtonEnabled, boolean isBackPressEnabled) {
        this.isOkButtonEnabled = isOkButtonEnabled;
        this.isRestartButtonEnabled = isRestartButtonEnabled;
        this.isBackPressEnabled = isBackPressEnabled;
    }
}
