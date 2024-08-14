# Thanks For Using Nicasso Library
## This library is similar to the popular Picasso library but it is more light weight approximately `7.83 kB.` and  efficient.
## Follow the documentation below to know how to use the library

### Step 0: Add this to your project gradle
#### `settings.gradle`
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        ...
        //add jitpack
        maven { url 'https://jitpack.io' }
    }
}
```
#### `settings.gradle.kts`
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        ...
        // add jitpack
        maven(url = "https://jitpack.io")
    }
}
```
#### `build.gradle` app level module
```groovy
dependencies 
{
  implementation 'com.github.CodeWithTamim:Nicasso:1.0.2'
}
```
#### `build.gradle.kts` app level module
```groovy
dependencies 
{
implementation("com.github.CodeWithTamim:Nicasso:1.0.2")
}
```

### If your min sdk is not 21 or different then add this to the `AndroidManifest.xml`

```xml

<uses-sdk android:minSdkVersion="your_min_sdk" tools:overrideLibrary="com.nasahacker.downloader" />
```

### Step 1 : Add Internet permission to your manifest

```xml
 <uses-permission android:name="android.permission.INTERNET"/>
 ```
 
### Step 3 : Load the image from URL to your image view
#### Java
```java
//load like this, NOTE: The default image is optional
Nicasso.get(url).default(R.drawable.ic_launcher_background).load(imageView = iamgeView);
 ```
 #### Kotlin
```kotlin
//load like this, NOTE: The default image is optional
Nicasso.get(url).default(R.drawable.ic_launcher_background).load(imageView = iamgeView)
 ```

### Thanks for reading the documentation, I'm `Tamim`, I made this library and I'm the one who was helping you throughout the documentation :)
### If the library helped you out then please give it a start and share with your dev friends ! The project is open for contrubution so if you have any fixes or new feature enhancement then just fork it then make your changes create a new brach and then just hit a pull request.

## Thank you guys for your love and support
## If you have any queries or need help then just open a issue or  <a href="mailto:tamimh.dev@gmail.com">mail me</a>
## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.


 