package com.deepdroid.coredev.devdialog.serviceurlselection;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class SelectableServiceUrlItem {
    private static final String TAG = SelectableServiceUrlItem.class.getSimpleName();
    public static final int ILLEGAL_SELECTION_RESULT = -1;

    public final int itemId;
    public int currentSelection = 0;
    public final String title;
    public final List<String> serviceUrlList;

    public SelectableServiceUrlItem(int itemId, int currentSelection, String title, String... serviceUrlList) {
        this.itemId = itemId;
        this.currentSelection = currentSelection;
        this.title = title;
        this.serviceUrlList = new ArrayList<>();

        if (serviceUrlList == null) {
            return;
        }
        for (String url : serviceUrlList) {
            if (TextUtils.isEmpty(url)) {
                continue;
            }
            this.serviceUrlList.add(url);
        }
    }

    public String getSelectedUrl() {
        if (!isIndexAvailable(currentSelection)) {
            return "";
        }
        return serviceUrlList.get(currentSelection);
    }

    public int setSelectedIndex(int newSelectionIndex) {
        if (!isIndexAvailable(newSelectionIndex)) {
            Log.println(Log.ASSERT, TAG, "Failed to set selection! Current selection is : " + currentSelection);
            return ILLEGAL_SELECTION_RESULT;
        }
        currentSelection = newSelectionIndex;
        return itemId;
    }

    private boolean isIndexAvailable(int index) {
        if (serviceUrlList == null || serviceUrlList.isEmpty()) {
            Log.println(Log.ASSERT, TAG, "Selectable service url list was empty!");
            return false;
        }
        if (currentSelection < 0 || currentSelection >= serviceUrlList.size()) {
            Log.println(Log.ASSERT, TAG, "Current selection index (" + currentSelection + ") was illegal! - List size is:" + serviceUrlList.size());
            return false;
        }
        return true;
    }
}
