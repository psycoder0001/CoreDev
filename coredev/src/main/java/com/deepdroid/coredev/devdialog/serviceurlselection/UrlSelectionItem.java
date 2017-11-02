package com.deepdroid.coredev.devdialog.serviceurlselection;

import java.io.Serializable;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class UrlSelectionItem implements Serializable {
    public int itemId;
    public int selectionIndex;
    public String selectionValue;

    public UrlSelectionItem(int itemId, int selectionIndex, String selectionValue) {
        this.itemId = itemId;
        this.selectionIndex = selectionIndex;
        this.selectionValue = selectionValue;
    }
}
