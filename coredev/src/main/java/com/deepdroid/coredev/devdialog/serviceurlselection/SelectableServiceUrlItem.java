package com.deepdroid.coredev.devdialog.serviceurlselection;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.deepdroid.coredev.HelperForPref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evrenozturk on 02.11.2017.
 */

public class SelectableServiceUrlItem {
    private static final String TAG = SelectableServiceUrlItem.class.getSimpleName();

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

    public UrlSelectionItem setSelectedIndex(int newSelectionIndex) {
        if (!isIndexAvailable(newSelectionIndex)) {
            Log.println(Log.ASSERT, TAG, "Failed to set selection! Current selection is : " + currentSelection);
            return null;
        }
        currentSelection = newSelectionIndex;
        return getSelectionItem();
    }

    private boolean isIndexAvailable(int index) {
        if (serviceUrlList == null || serviceUrlList.isEmpty()) {
            Log.println(Log.ASSERT, TAG, "Selectable service selectionValue list was empty!");
            return false;
        }
        if (index < 0 || index >= serviceUrlList.size()) {
            Log.println(Log.ASSERT, TAG, "Current selection index (" + index + ") was illegal! - List size is:" + serviceUrlList.size());
            return false;
        }
        return true;
    }

    public UrlSelectionItem getSelectionItem() {
        String selectionValue = getSelectedUrl();
        if (TextUtils.isEmpty(selectionValue)) {
            Log.println(Log.ASSERT, TAG, "Selection value was empty");
            return null;
        }
        return new UrlSelectionItem(itemId, currentSelection, selectionValue);
    }

    boolean loadSelection(Context appCx) {
        UrlSelectionItem selectionItem = HelperForPref.getUrlSelection(appCx, itemId);
        if (selectionItem == null) {
            return false;
        }
        currentSelection = selectionItem.selectionIndex;
        if (serviceUrlList == null || serviceUrlList.isEmpty() || selectionItem.selectionIndex < 0 || selectionItem.selectionIndex >= serviceUrlList.size()) {
            return false;
        }
        serviceUrlList.set(currentSelection, selectionItem.selectionValue);
        return true;
    }
}
