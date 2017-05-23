package com.deepdroid.coredev.devdialog;

import com.deepdroid.coredev.devdialog.uifordevdialog.DevelopmentDialogCustomOptionsListener;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public class DevelopmentDialogData {
    public DevelopmentDialogCustomOptionsListener devDialogListener;

    public DevelopmentDialogData(DevelopmentDialogCustomOptionsListener developmentDialogListener) {
        this.devDialogListener = developmentDialogListener;
    }
}
