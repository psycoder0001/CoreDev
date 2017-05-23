package com.deepdroid.coredev.corepicker;

/**
 * Created by evrenozturk on 23/06/15.
 */
public interface CorePickerListener {
    void onCorePickerCanceled(int selectedIndex);

    void onCorePickerConfirmed(int selectedIndex);
}
