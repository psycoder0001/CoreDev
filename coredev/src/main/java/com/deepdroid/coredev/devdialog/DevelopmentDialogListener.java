package com.deepdroid.coredev.devdialog;

import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentItem;

import java.util.List;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public abstract class DevelopmentDialogListener {

    public abstract boolean isDevelopmentVersion();

    public abstract List<CustomDevelopmentItem> getCustomOptionsList();

    public abstract SelectableServiceUrlData getServiceUrlLists();

    public abstract void onSelectionChanged(int newSelectionItemId, int newSelectionIndex, String newSelectionValue);

    public abstract void onCustomCheckChanged(int index, boolean isChecked);

    public abstract void onCustomButtonClicked(int index);
}
