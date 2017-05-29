package com.deepdroid.coredev.devdialog.uifordevdialog;

import java.util.List;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public abstract class DevelopmentDialogListener {

    public abstract boolean isDevelopmentVersion();
    
    public abstract List<CustomDevelopmentItem> getCustomOptionsList();

    public abstract String[] getServiceUrlList();

    public abstract String getServiceUrlAt(int selectedIndex);

    public abstract String getCurrentServiceUrl();

    public abstract void onServiceUrlChanged(String selectedServiceUrl);

    public abstract void onCustomCheckChanged(int index, boolean isChecked);

    public abstract void onCustomButtonClicked(int index);
}
