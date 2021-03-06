package com.deepdroid.coredev.devdialog.serviceurlselection;

import android.content.Context;
import android.util.Log;

import com.deepdroid.coredev.corepicker.CorePickerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class SelectableServiceUrlData {
    private static final String TAG = SelectableServiceUrlData.class.getSimpleName();

    public List<SelectableServiceUrlItem> selectableServiceUrlItemList;

    public SelectableServiceUrlData(Context applicationContext, SelectableServiceUrlItem... selectableServiceUrlItemList) {
        this.selectableServiceUrlItemList = new ArrayList<>();
        if (selectableServiceUrlItemList == null) {
            return;
        }
        for (SelectableServiceUrlItem selectableServiceUrlItem : selectableServiceUrlItemList) {
            if (selectableServiceUrlItem == null) {
                continue;
            }
            this.selectableServiceUrlItemList.add(selectableServiceUrlItem);
        }

        loadPreviousSelections(applicationContext);
    }

    public CorePickerItem getSelectedUrlItemAt(int index) {
        if (!isIndexAvailable(index)) {
            return null;
        }
        return selectableServiceUrlItemList.get(index).getSelectedUrl();
    }

    public UrlSelectionItem setNewSelectionIndexAt(int index, int newSelectionIndex) {
        if (!isIndexAvailable(index)) {
            return null;
        }
        return selectableServiceUrlItemList.get(index).setSelectedIndex(newSelectionIndex);
    }

    public List<CorePickerItem> getUrlListAt(int index) {
        if (!isIndexAvailable(index)) {
            return new ArrayList<>();
        }
        return selectableServiceUrlItemList.get(index).serviceUrlList;
    }

    private boolean isIndexAvailable(int index) {
        if (selectableServiceUrlItemList == null || selectableServiceUrlItemList.isEmpty()) {
            Log.println(Log.ASSERT, TAG, "Selectable service selectionValue list was empty!");
            return false;
        }
        if (index < 0 || index >= selectableServiceUrlItemList.size()) {
            Log.println(Log.ASSERT, TAG, "Given index (" + index + ") was illegal! - List size is:" + selectableServiceUrlItemList.size());
            return false;
        }
        return true;
    }

    private boolean loadPreviousSelections(Context appCx) {
        if (selectableServiceUrlItemList == null || selectableServiceUrlItemList.isEmpty()) {
            return false;
        }
        boolean hasPreviousSelection = false;
        for (SelectableServiceUrlItem selectableServiceUrlItem : selectableServiceUrlItemList) {
            if (selectableServiceUrlItem == null) {
                continue;
            }
            if (selectableServiceUrlItem.loadSelection(appCx)) {
                hasPreviousSelection = true;
            }
        }
        return hasPreviousSelection;
    }
}
