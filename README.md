# CoreDev

1. Add next lines in your main build.gradle file:
allprojects {
    repositories {
        maven { url "http://dl.bintray.com/psycoder0001/AndroidLibs" }
    }
}

2. Add next line in project's build.gradle file:
    compile 'com.deepdroid.coredev:coredev:1.0.4'
