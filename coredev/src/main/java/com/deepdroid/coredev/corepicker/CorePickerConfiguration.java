package com.deepdroid.coredev.corepicker;

/**
 * Created by evrenozturk on 23/06/15.
 */
public class CorePickerConfiguration {
    public int layoutResourceId;
    public int listContainerId;
    public int customAnimStyleId = -1;

    public int itemResourceId;
    public int itemTitleId;
    public int itemValueId;
    public int itemRootId;
    public int itemSelectedTitleId;
    public int itemSelectedValueId;
    public int itemSelectedRootId;

    public void set(int layoutResourceID, int listContainerID, int customAnimStyleId
            , int itemResourceId
            , int itemRootId, int itemTitleId, int itemValueId
            , int itemSelectedRootId, int itemSelectedTitleId, int itemSelectedValueId) {
        this.layoutResourceId = layoutResourceID;
        this.listContainerId = listContainerID;
        this.customAnimStyleId = customAnimStyleId;
        this.itemResourceId = itemResourceId;
        this.itemRootId = itemRootId;
        this.itemTitleId = itemTitleId;
        this.itemValueId = itemValueId;
        this.itemSelectedRootId = itemSelectedRootId;
        this.itemSelectedTitleId = itemSelectedTitleId;
        this.itemSelectedValueId = itemSelectedValueId;
    }
}