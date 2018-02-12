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
    implementation 'com.deepdroid.coredev:coredev:1.0.16'
```

### II. How to use?
II.A. Attach to your activity root.
```java
    protected void initDevelopmentDialog(ViewGroup activityRootView) {
        DevelopmentDialogListener developmentDialogListener = new DevelopmentDialogListener() {
            // ... Overriden abstract methods
            // ...
        };
        DevUFO.attachUfo(BaseActivity.this, activityRootView, null, developmentDialogListener);
    }
```
II.B. Adding selectable urls.

II.B.1. * Basic case where you have single api in your app.
 ```java
    @Override
    public SelectableServiceUrlData getServiceUrlLists() {
        return new SelectableServiceUrlData(applicationContext
                , new SelectableServiceUrlItem(API_MOBIL, defaultServiceUrlIndex, "Mobil Api"
                , "http://prd.yourmobilapi.com.tr/api/"
                , "http://tst.yourmobilapi.com.tr/api/"
                , "http://dev.yourmobilapi.com.tr/api/")
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
                , "http://prd.yourmobilapi.com.tr/api/"
                , "http://tst.yourmobilapi.com.tr/api/"
                , "http://dev.yourmobilapi.com.tr/api/")
                , new SelectableServiceUrlItem(API_SOCKET, defaultServiceUrlIndex, "Socket Api"
                , "http://prd.yoursocketapi.com.tr/api/"
                , "http://tst.yoursocketapi.com.tr/api/"
                , "http://dev.yoursocketapi.com.tr/api/")
                , new SelectableServiceUrlItem(API_PUSH, defaultServiceUrlIndex, "Push Api"
                , "http://prd.yourpushapi.com.tr/api/"
                , "http://tst.yourpushapi.com.tr/api/"
                , "http://dev.yourpushapi.com.tr/api/")
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
        final List<CustomDevelopmentItem> customCheckList = new ArrayList<>();
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.IS_AD_ENABLED, "Ads Enabled"));
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.IS_ANALYTICS_ENABLED, "Analytics Enabled"));
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.IS_FIREBASE_ENABLED, "Firebase Enabled"));
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.LOGS_ENABLED_FOR_ADS, "Logs Enabled for Ads"));
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.LOGS_ENABLED_FOR_ANALYTICS, "Logs Enabled for Analytics"));
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.LOGS_ENABLED_FOR_LOCATION, "Logs Enabled for Location"));
        customCheckList.add(new CustomDevelopmentCheckItem(AppConstants.IS_ADS_DEVELOPMENT_MODE_ENABLED, "Development Mode for Ads"));
        customCheckList.add(new CustomDevelopmentButtonItem("Show Rate Me Dialog"));
        customCheckList.add(new CustomDevelopmentButtonItem("Show Firebase Token"));
        return customCheckList;
    }
    
    @Override
    public void onCustomCheckChanged(int index, boolean isChecked) {
        switch (index) {
            case 0:
                HConstants.IS_AD_ENABLED = isChecked;
                break;
            case 1:
                HConstants.IS_ANALYTICS_ENABLED = isChecked;
                break;
            case 2:
                HConstants.IS_FIREBASE_ENABLED = isChecked;
                break;
            case 3:
                HConstants.LOGS_ENABLED_FOR_ADS = isChecked;
                break;
            case 4:
                HConstants.LOGS_ENABLED_FOR_ANALYTICS = isChecked;
                break;
            case 5:
                HConstants.LOGS_ENABLED_FOR_LOCATION = isChecked;
                break;
            case 6:
                HConstants.IS_ADS_DEVELOPMENT_MODE_ENABLED = isChecked;
                break;
        }
    }

    @Override
    public void onCustomButtonClicked(int index) {
        if (customOptionsListener == null) {
            return;
        }
        switch (index) {
            case 7:
                customOptionsListener.onRateMeClicked();
                break;
            case 8:
                customOptionsListener.onFirebaseTokenClicked();
                break;
        }
    }
```
