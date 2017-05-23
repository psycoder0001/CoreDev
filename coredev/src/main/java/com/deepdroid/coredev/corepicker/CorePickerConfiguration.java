package com.deepdroid.coredev.corepicker;

/**
 * Created by evrenozturk on 23/06/15.
 */
public class CorePickerConfiguration {
    public int layoutResourceID;
    public int listContainerID;
    public int customAnimStyleId = -1;

    public int itemResourceID;
    public int itemTextID;
    public int itemRootID;
    public int itemSelectedTextID;
    public int itemSelectedRootID;

    public void set(int layoutResourceID, int listContainerID, int customAnimStyleId
            , int itemResourceID, int itemTextID, int itemRootID, int itemSelectedTextID, int itemSelectedRootID) {
        this.layoutResourceID = layoutResourceID;
        this.listContainerID = listContainerID;
        this.customAnimStyleId = customAnimStyleId;
        this.itemResourceID = itemResourceID;
        this.itemTextID = itemTextID;
        this.itemRootID = itemRootID;
        this.itemSelectedTextID = itemSelectedTextID;
        this.itemSelectedRootID = itemSelectedRootID;
    }
}
