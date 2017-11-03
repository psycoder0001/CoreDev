# CoreDev
This library aims to make your development & test process faster & easier.
It's a small green circle over your app.

**Features:**
* Easy service url selection.
* Editable service url area.
* Customizable checkables & Buttons. (You'll get a notification when those items are itracted with. So you can take action in your app.)

### How to add in the project?
1. Add next lines in your **projects build.gradle** file:
```
allprojects {
    repositories {
        maven { url "http://dl.bintray.com/psycoder0001/AndroidLibs" }
    }
}
```

2. Add next line in modules build.gradle file:
```
    implementation 'com.deepdroid.coredev:coredev:1.0.4'
```

### How to use?
1. Attach to your activity root.
```java
    protected void initDevelopmentDialog(ViewGroup activityRootView) {
        DevelopmentDialogListener developmentDialogListener = new DevelopmentDialogListener() {
            // ... Overriden abstract methods
            // ...
        };
        DevUFO.attachUfo(BaseActivity.this, activityRootView, null, developmentDialogListener);
    }
```
2. Adding selectable urls.
 * Basic case where you have single api in your app.
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

 * Complex case where : you are using 3 or more different api in your app:
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
                , "http://yourmobilapi.com.tr/api/"
                , "http://prd.yourmobilapi.com.tr/api/"
                , "http://dev.yourmobilapi.com.tr/api/")
                , new SelectableServiceUrlItem(API_PUSH, defaultServiceUrlIndex, "Push Api"
                , "http://prd.yourmobilapi.com.tr/api/"
                , "http://tst.yourmobilapi.com.tr/api/"
                , "http://dev.yourpushapi.com.tr/api/")
        );
    }
```
 And handle its actions just like that:
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
That's all your service urls are now ready to be selection during test or development.


