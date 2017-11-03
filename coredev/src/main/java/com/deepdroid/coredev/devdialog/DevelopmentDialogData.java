package com.deepdroid.coredev.devdialog;

import com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlData;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public class DevelopmentDialogData {
    public final SelectableServiceUrlData selectableServiceUrlData;
    public DevelopmentDialogListener devDialogListener;

    public DevelopmentDialogData(SelectableServiceUrlData selectableServiceUrlData, DevelopmentDialogListener developmentDialogListener) {
        this.selectableServiceUrlData = selectableServiceUrlData;
        this.devDialogListener = developmentDialogListener;
    }

}
