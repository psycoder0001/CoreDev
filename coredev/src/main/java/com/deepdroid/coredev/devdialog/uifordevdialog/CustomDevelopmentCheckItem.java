package com.deepdroid.coredev.devdialog.uifordevdialog;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public class CustomDevelopmentCheckItem implements CustomDevelopmentItem{
    public boolean isChecked;
    public String text;

    public CustomDevelopmentCheckItem(boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
    }
}
