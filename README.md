# CoreDev
This library aims to make your development & test process faster & easier.
It's a small green circle over your app.

**Features:**
* Easy service url selection.
* Editable service url area.
* Customizable checkables & Buttons. (You'll get a notification when those items are itracted with. So you can take action in your app.)

### I. How to add in the project?
I.A. Add next lines in your **projects build.gradle** file:
```
allprojects {
    repositories {
        maven { url "http://dl.bintray.com/psycoder0001/AndroidLibs" }
    }
}
```

I.B. Add next line in modules build.gradle file:
```
    implementation 'com.deepdroid.coredev:coredev:1.0.26'
```

### II. How to use?
II.A. Attach to your activity root.
```java
    protected void initDevelopmentDialog(ViewGroup activityRootView) {
        DevelopmentDialogListener developmentDialogListener = new DevelopmentDialogListener() {
            // ... Overriden abstract methods
            // ...
        };
        DevelopmentDialogData devDialogData = new DevelopmentDialogData(true, true, false); // Can be null for default params
        DevUFO.attachUfo(BaseActivity.this, activityRootView, null, developmentDialogListener, devDialogData);
    }
```
II.B. Adding selectable urls.

II.B.1. * Basic case where you have single api in your app.
 ```java
    @Override
    public SelectableServiceUrlData getServiceUrlLists() {
        return new SelectableServiceUrlData(applicationContext
                , new SelectableServiceUrlItem(API_MOBIL, defaultServiceUrlIndex, "Mobil Api"
                , new CorePickerItem("Potato", "http://prd.yourmobilapi.com.tr/api/")
                , new CorePickerItem("Tomato", "http://tst.yourmobilapi.com.tr/api/")
                , new CorePickerItem("Garlic", "http://dev.yourmobilapi.com.tr/api/"))
        );
    }
    
    @Override
    public void onSelectionChanged(UrlSelectionItem selectionItem) {
        if (selectionItem == null) {
            return;
        }
        ApiConstants.BASE_MOBIL_API_URL = selectionItem.selectionValue;
    }
```

 II.B.1. * Complex case where : you are using 3 or more different api in your app:
 Lets assume there are Mobil, Socket & Push apis.
 All of them has 3 different evironments PROD, TEST & DEV.
 You will need to initialize your SelectableServiceUrlData like this:
```java
    @Override
    public SelectableServiceUrlData getServiceUrlLists() {
        return new SelectableServiceUrlData(applicationContext
                , new SelectableServiceUrlItem(API_MOBIL, defaultServiceUrlIndex, "Mobil Api"
                , new CorePickerItem("M PROD", "http://prd.yourmobilapi.com.tr/api/")
                , new CorePickerItem("MTEST", "http://tst.yourmobilapi.com.tr/api/")
                , new CorePickerItem("M DEV", "http://dev.yourmobilapi.com.tr/api/"))
                , new SelectableServiceUrlItem(API_SOCKET, defaultServiceUrlIndex, "Socket Api"
                , new CorePickerItem("S PROD", "http://prd.yoursocketapi.com.tr/api/")
                , new CorePickerItem("S TEST", "http://tst.yoursocketapi.com.tr/api/")
                , new CorePickerItem("S DEV", "http://dev.yoursocketapi.com.tr/api/"))
                , new SelectableServiceUrlItem(API_PUSH, defaultServiceUrlIndex, "Push Api"
                , new CorePickerItem("P PROD", "http://prd.yourpushapi.com.tr/api/")
                , new CorePickerItem("P TEST", "http://tst.yourpushapi.com.tr/api/")
                , new CorePickerItem("P DEV", "http://dev.yourpushapi.com.tr/api/"))
        );
    }
```
 And handle your actions just like that:
```java
    @Override
    public void onSelectionChanged(UrlSelectionItem selectionItem) {
        if (selectionItem == null) {
            return;
        }
        switch (selectionItem.itemId) {
            case API_MOBIL:
                ApiConstants.BASE_MOBIL_API_URL = selectionItem.selectionValue;
                break;
            case API_SOCKET:
                ApiConstants.BASE_SOCKET_API_URL = selectionItem.selectionValue;
                break;
            case API_PUSH:
                ApiConstants.BASE_PUSH_API_URL = selectionItem.selectionValue;
                break;
        }
    }
```
That's all! Your service urls are now ready to be selected during test or development.

II.C. Adding custom development options.
```java
// Thease are just examples. You can add your custom option for development.
    @Override
    public List<CustomDevelopmentItem> getCustomOptionsList() {
        final List<CustomDevelopmentItem> customItemList = new ArrayList<>();
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_0, AppConstants.IS_AD_ENABLED, "Ads Enabled"));
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_1, AppConstants.IS_ANALYTICS_ENABLED, "Analytics Enabled"));
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_2, AppConstants.IS_FIREBASE_ENABLED, "Firebase Enabled"));
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_3, AppConstants.LOGS_ENABLED_FOR_ADS, "Logs Enabled for Ads"));
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_4, AppConstants.LOGS_ENABLED_FOR_ANALYTICS, "Logs Enabled for Analytics"));
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_5, AppConstants.LOGS_ENABLED_FOR_LOCATION, "Logs Enabled for Location"));
        customItemList.add(new CustomDevelopmentCheckItem(ID_ITEM_CHK_6, AppConstants.IS_ADS_DEVELOPMENT_MODE_ENABLED, "Development Mode for Ads"));
        customItemList.add(new CustomDevelopmentButtonItem(ID_ITEM_BTN_0, "Show Rate Me Dialog"));
        customItemList.add(new CustomDevelopmentButtonItem(ID_ITEM_BTN_1, "Show Firebase Token"));
        return customItemList;
    }
    
    @Override
    public void onCustomCheckChanged(CustomDevelopmentCheckItem customDevelopmentCheckItem) {
        switch (customDevelopmentCheckItem.id) {
            case ID_ITEM_CHK_0:
                HConstants.IS_AD_ENABLED = isChecked;
                break;
            case ID_ITEM_CHK_1:
                HConstants.IS_ANALYTICS_ENABLED = isChecked;
                break;
            case ID_ITEM_CHK_2:
                HConstants.IS_FIREBASE_ENABLED = isChecked;
                break;
            case ID_ITEM_CHK_3:
                HConstants.LOGS_ENABLED_FOR_ADS = isChecked;
                break;
            case ID_ITEM_CHK_4:
                HConstants.LOGS_ENABLED_FOR_ANALYTICS = isChecked;
                break;
            case ID_ITEM_CHK_5:
                HConstants.LOGS_ENABLED_FOR_LOCATION = isChecked;
                break;
            case ID_ITEM_CHK_6:
                HConstants.IS_ADS_DEVELOPMENT_MODE_ENABLED = isChecked;
                break;
        }
    }

    @Override
    public void onCustomButtonClicked(CustomDevelopmentButtonItem customDevelopmentButtonItem) {
        if (customOptionsListener == null) {
            return;
        }
        switch (customDevelopmentButtonItem.id) {
            case ID_ITEM_BTN_0:
                customOptionsListener.onRateMeClicked();
                break;
            case ID_ITEM_BTN_1:
                customOptionsListener.onFirebaseTokenClicked();
                break;
        }
    }
```
