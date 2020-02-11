Connect To Web With Volley
//Step 1 : Add Volley to project is to add the following dependency to app`s build.gradle file
dependencies {
    ...
    implementation 'com.android.volley:volley:1.1.1'
    ...
}

//Step 2 : Add the INTERNET permission
Add the android.permission.INTERNER permission to app`s manifest
<manifest ...
    <uses-permission android:name="android.permission.INTERNET" />
</manifest>



Serialization and Deserialization JSON With GSON
//Step 1 : Add GSON to project is to build.gradle file
dependencies {
    ...
    implementation 'com.google.code.gson:gson:2.8.5'
    ...
}

