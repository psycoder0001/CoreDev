package com.deepdroid.coredev.devdialog.serviceurlselection;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.deepdroid.coredev.devdialog.serviceurlselection.SelectableServiceUrlItem.ILLEGAL_SELECTION_RESULT;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class SelectableServiceUrlData {
    private static final String TAG = SelectableServiceUrlData.class.getSimpleName();

    public List<SelectableServiceUrlItem> selectableServiceUrlItemList;

    public SelectableServiceUrlData(SelectableServiceUrlItem... selectableServiceUrlItemList) {
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
    }

    public String getSelectedUrlAt(int index) {
        if (!isIndexAvailable(index)) {
            return "";
        }
        return selectableServiceUrlItemList.get(index).getSelectedUrl();
    }

    public int setNewSelectionIndexAt(int index, int newSelectionIndex) {
        if (!isIndexAvailable(index)) {
            return ILLEGAL_SELECTION_RESULT;
        }
        return selectableServiceUrlItemList.get(index).setSelectedIndex(newSelectionIndex);
    }

    public List<String> getUrlListAt(int index) {
        if (!isIndexAvailable(index)) {
            return new ArrayList<>();
        }
        return selectableServiceUrlItemList.get(index).serviceUrlList;
    }

    private boolean isIndexAvailable(int index) {
        if (selectableServiceUrlItemList == null || selectableServiceUrlItemList.isEmpty()) {
            Log.println(Log.ASSERT, TAG, "Selectable service url list was empty!");
            return false;
        }
        if (index < 0 || index >= selectableServiceUrlItemList.size()) {
            Log.println(Log.ASSERT, TAG, "Given index (" + index + ") was illegal! - List size is:" + selectableServiceUrlItemList.size());
            return false;
        }
        return true;
    }
}
