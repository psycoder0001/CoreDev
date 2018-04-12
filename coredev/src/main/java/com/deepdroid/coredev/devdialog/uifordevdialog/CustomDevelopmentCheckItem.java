package com.deepdroid.coredev.devdialog.uifordevdialog;

/**
 * Created by evrenozturk on 07.11.2016.
 */
public class CustomDevelopmentCheckItem implements CustomDevelopmentItem {
    public String id;
    public boolean isChecked;
    public String text;

    public CustomDevelopmentCheckItem(String id, boolean isChecked, String text) {
        this.id = id;
        this.isChecked = isChecked;
        this.text = text;
    }
}
