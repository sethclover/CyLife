# android_unit3_2

### What is this example

A Volley example that contains the following example requests:

1. Upload Image as Multipart file

2. Image request - Download an bitmap image and display


### Setup

- BACKEND: change "directory" variable in `ImageController.java`, make sure there is a `/` in the end

- FRONTEND: add `MultipartRequest.java` (Custom Volley Request to handle Multipart files) to your project, then follow `ImageUploadActivity.java`


### Important Notes

- Use `10.0.2.2` instead of `localhost` IF the server program is running on the same host as the Android Enmulator

- AndroidManifest.xml
    - add `<uses-permission android:name="android.permission.INTERNET" />` before `<application>`
    - add `android:usesCleartextTraffic="true"` inside `<application>`

- build.gradle (Module :app)
    - add `implementation 'com.android.volley:volley:1.2.1'` inside `dependencies{}`, then sync gradle.

### Version Tested

|Android Studio            | Android SDK | Gradle | Gradle Plugin | Gradle JDK | Emulator |
|--------------------------|-------------|--------|---------------|------------|----------|
|Giraffe 2022.3.1 Patch 2  |     33      | 8.0.0  |    8.0.0      |    17      | Pixel 7  |

|IntelliJ  | Project SDK | Springboot | Maven |
|----------|-------------|------------|-------|
|2023.2.2  |     17      | 3.1.4      | 3.6.3 |

