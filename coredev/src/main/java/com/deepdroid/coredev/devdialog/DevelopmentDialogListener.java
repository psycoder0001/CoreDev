package com.deepdroid.coredev.devdialog;

import android.view.View;
import android.view.ViewGroup;

import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;
import com.deepdroid.coredev.devdialog.serviceurlselection.UrlSelectionItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentButtonItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentCheckItem;
import com.deepdroid.coredev.devdialog.uifordevdialog.CustomDevelopmentItem;

import java.util.List;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public abstract class DevelopmentDialogListener {

    public abstract boolean isDevelopmentVersion();

    public abstract SelectableServiceUrlData getServiceUrlLists();

    public abstract void onSelectionChanged(UrlSelectionItem selectionItem);

    public abstract List<CustomDevelopmentItem> getCustomOptionsList();

    public abstract void onCustomCheckChanged(CustomDevelopmentCheckItem customDevelopmentCheckItem);

    public abstract void onCustomButtonClicked(CustomDevelopmentButtonItem customDevelopmentButtonItem);

    public View getCustomBackgroundView(ViewGroup parent) {
        return null;
    }
}
